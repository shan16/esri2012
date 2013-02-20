#!/usr/bin/env node

console.log("Server dnode program.");

// LabServer:
var DNode = require('dnode');

var serverNode = DNode({
  transmitEvent: function(eventInfo, cb) {
    console.log("Event receieved: ",eventInfo);
    cb();
  }
});

[6060].forEach(function(port) {
  serverNode.connect(port,function(labMachine) {
    labMachine.startEventStream(port);
  });
});

