#!/usr/bin/env node
/*
var app = require('express').createServer()
  , io = require('socket.io').listen(app);

app.listen(80);
*/

var express = require('express')
  , http = require('http')
  , app = express()
  , server = http.createServer(app)
  , io = require('socket.io').listen(server);

server.listen(8080);

app.get('/', function (req, res) {
  res.sendfile(__dirname + '/expressIndex.html');
});

io.sockets.on('connection', function (socket) {
  setInterval(function() {
    socket.emit('news', { Hello: 'here is the', time: (Date()).toString() });
  }, 2000); 
});
