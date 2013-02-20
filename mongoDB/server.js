#!/usr/bin/env node

// server.js. This class plays the role of server, which connects the lab machines and browesers, also communicates with database.

var mongo = require('mongodb'),
  Server = mongo.Server,
  Db = mongo.Db;

var server = new Server('localhost', 27017, {auto_reconnect: true});
var db = new Db('mydb', server);



require('sugar');

var 
  redis = require("redis"),
  client = redis.createClient(),
  channels = [];

// Initialize channels, only for testing.
for(i = 0; i < 10; i++){

  channels[i] = "QA900" + i;

}

console.log("waiting for messages...");


var express = require('express')
  , http = require('http')
  , app = express()
  , server = http.createServer(app)
  , io = require('socket.io').listen(server);


// Subscribe after "ready"
client.on("ready", function(){

  // Use client.subscribe("channel_.*"); for multiple channels
  client.subscribe(channels);

  server.listen(8080);

  app.get('/', function (req, res) {
    res.sendfile(__dirname + '/index.html');
  });
  
});

var openSockets = {};


// Database connection
db.open(function(err,db){

  // Receive messages
  io.sockets.on('connection', function(socket) {
    openSockets[socket.id] = socket;
  
    // Query all machines' current status when a new browser connected to server.
    if(!err) {
      console.log("Server connected to mydb.");
      db.collection('machines', function(err, collection) {
        collection.find().toArray(function(err, items){
          
          // For each item, send the initial info to browser
          for(i = 0; i < items.length; i++){
            // initial_info
            socket.emit('initial_info', {
              from: items[i].key,
              sample_value: items[i].sample_value,
              last_update: items[i].last_update,
              OS_name: items[i].OS_name,
              OS_version: items[i].OS_version,
              Initial_Info: items[i]
            });
          }
        });
      });
    }

    socket.on('disconnect', function() {
      delete openSockets[socket.id];
    });
  });

  // Start to calculate updating rate
  var logID = 0,
      lastID = 0,
      rate = 0,
      lastRate = 0;
  
  // Use number of logs(messages) during a second to calculate the rate.
  setInterval(function(){
    lastID = logID;
    lastRate = rate;
    // Algorithm to compute the rate for this second.
    rate = Math.ceil((lastRate*0.7 + logID*0.3)*100)/100;
    logID = 0;
    io.sockets.emit('updateRate', {
      rate: rate
    });
  },1000);

  // When it receives a published event from any machine.
  client.on("message", function(channelName, message) {

    if(!err){
      logID++;
      console.log(channelName + " connected to mydb");
      db.collection('machines', function(err, collection){
        collection.find().toArray(function(err, items){

          // Perform database update.
          collection.update({key: channelName}, {$set: {sample_value: (JSON.parse(message)).sample_value}});

          var new_date = (new Date()).toString();
          collection.update({key: channelName}, {$set: {last_update: (JSON.parse(message)).last_update}});

          // Send updated info to browser.
          Object.each(openSockets,function(socketId, socket) {
            // update
            socket.emit('update', {
              from: channelName,
              logID: logID,
              sample_value: (JSON.parse(message)).sample_value,
              last_update: (JSON.parse(message)).last_update,
              message: (JSON.parse(message)).message
            });
          });
        });
      });
    } // End if
  });
});// End db.open

