#!/usr/bin/env node

var readline = require('readline'),
rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

console.log("Machine dnode program.");

// LabMachine
var DNode = require('dnode');

[6060].forEach(function(port) {
DNode(function (controllerMachine) {
  var machineContext = this;
  
  this.startEventStream = function(portNumber) {
    setTimeout(function() {  
      controllerMachine.transmitEvent({
        time: new Date().getTime(),
        health: 23,
        powers: 12
      },function() {
        machineContext.startEventStream(portNumber);
      });
    },5000);
  };

}).listen(port);
});


