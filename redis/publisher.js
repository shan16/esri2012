#!/usr/bin/env node

// This script plays the role of publisher.
require('sugar');

var 
  redis = require("redis"),
  client = redis.createClient();
 
// Generate random channels for different publishers, only for testing.
var channelName = "channel_" + Math.ceil(Math.random()*100).toString();

// Publish after "ready"
client.on("ready", function(){
  
  setInterval(function () {

    // The message now is a JSON object
    var message = "The time is " + (new Date()).toString() + ', ' +
	(0).upto(1).map(function(i) { return "item " + i; }).join(',');

    // Publish a message once per 2 seconds 
    client.publish(channelName, JSON.stringify({
        health: 20,
        powers: 4,
        life: 9,
        message: message
    }), function (err, reply) {

        console.log("From: [" + channelName + "]");
        console.log("Published message to " +
          (reply === 0 ? "no one\n" : (reply + " subscriber(s).\n")));

      });

  }, 2000);

});

