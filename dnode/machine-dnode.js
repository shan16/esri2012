#!/usr/bin/env node

console.log("Machine dnode program.");

var dnode = require('dnode'),
  net = require('net'),

lmListener = net.createServer(function(c) {
  var d = dnode({
    event: function(s,cb) {
      setTimeout(function() {
        cb("The epoch time is: "+new Date().getTime());
      },5000);
    }
  });

  c.pipe(d).pipe(c);
});

lmListener.listen(5004);

