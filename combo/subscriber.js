#!/usr/bin/env node

// This script plays the role of listener/subscriber/consumer to **all** channels/classes.

require('sugar');

var 
  redis = require("redis"),
  client = redis.createClient(),
  channels = [];

// Initialize channels, only for testing.
for(i = 0; i < 100; i++){

  channels[i] = "QA_90" + i;

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

// Receive messages
io.sockets.on('connection', function(socket) {
  openSockets[socket.id] = socket;
  
  socket.on('disconnect', function() {
     delete openSockets[socket.id];
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

