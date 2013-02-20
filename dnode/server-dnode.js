#!/usr/bin/env node

console.log("Server dnode program.");

var dnode = require('dnode'),
  net = require('net'),

d = dnode();

function log() {
  //todo: log to file
  console.log(Array.prototype.slice.call(arguments).join(' '));
}

d.on('remote', function(remote) {
  remote.event(null,function(eventReceived) {
    log("Event received:",eventReceived);
  });
});

var c = net.connect(5004);
c.pipe(d).pipe(c);


