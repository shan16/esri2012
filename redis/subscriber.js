#!/usr/bin/env node

// This script plays the role of listener/subscriber/consumer to **all** channels/classes.

var 
  redis = require("redis"),
  client = redis.createClient(),
  channels = [];

// Initialize channels, only for testing.
for(i = 1; i <= 100; i++){

  channels[i] = "channel_" + i;

}


console.log("waiting for messages...");


// Subscribe after "ready"
client.on("ready", function(){

  // Use client.subscribe("channel_.*"); for multiple channels
  client.subscribe(channels);

});


// Receive messages
client.on("message", function(channelName, message){

  // JSON object
  console.log("Subscribe ["+channelName+"]: ",JSON.parse(message));

});


