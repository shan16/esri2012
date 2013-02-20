#!/usr/bin/env node

// subscriber.js. This class plays the role of server, which connects the lab machines and browesers, also communicates with database.

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
          Object.each(openSockets,function(socketId, socket) {
            socket.emit('news', {
              msg: items
            });
          });
        });
      });
    }



    socket.on('disconnect', function() {
      delete openSockets[socket.id];
    });
  });
});

client.on("message", function(channelName, message) {
  Object.each(openSockets,function(socketId, socket) {
    socket.emit('news', {
      from: channelName,
      msg: JSON.parse(message)
    });
  });
});

