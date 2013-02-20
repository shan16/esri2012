#!/usr/bin/env node

// machines.js. This script plays the role of publisher.

var mongo = require('mongodb'),
  Server = mongo.Server,
  Db = mongo.Db;

var server = new Server('localhost', 27017, {auto_reconnect: true});
var db = new Db('mydb', server);


require('sugar');

var 
  redis = require("redis"),
  client = redis.createClient();
 
// Generate random channels for different machines, only for testing.
var randomMachine = Math.floor(Math.random()*8 + 1);
// Choose the machine using the second argument.
// If no second arg, or the number is out of bound, random number is used.
var machineNum = process.argv[2];
var channelName = (machineNum!=null && machineNum >=1 && machineNum <=8)? "QA900" + machineNum.toString() : "QA900" + randomMachine;


// Publish after "ready"
client.on("ready", function(){
  
  // No actual need for connecting the machine to db.
  // The machine would give its initial info itself.
  // Here we just querying the info out for practicing.
  db.open(function(err, db) {
    if(!err) {
      console.log(channelName + " connected to server.");
      db.collection('machines', function(err, collection) {
        
        // Find all initial information about this machine
        collection.find({key: channelName}).toArray(function(err, initInfo){
          console.log("Sending machineInfo: " + initInfo[0].key);
        
          // Send all initial information about this machine.
          client.publish(channelName, JSON.stringify({
            message: "NEW CONNECTION!",
            sample_value: initInfo[0].sample_value,
            last_update: initInfo[0].last_update
            }),function(err, reply){
               console.log("Initial status published!");
          });
        });
        
        changeStatus();
       
      });
    }
  });
});


function changeStatus(){

  var randomTime = Math.floor(Math.random()*4000+1000);
  var randomNum = Math.floor(Math.random()*100);

  setTimeout(function(){

    var new_date = (new Date()).toString();
    var message = "sample_value changed to " + randomNum + ', '                           + "last_update " + new_date + ', ' +
              (0).upto(50).map(function(i) { return "item " + i; }).join(',');

    // Send partial update to server.
    client.publish(channelName, JSON.stringify({
      sample_value: randomNum,
      last_update: new_date,
      message: message
    }), function (err, reply) {

          // Consule output for debugging
          console.log("From: [" + channelName + "]");
          console.log("Published message to " +
            (reply === 0 ? "no one\n" : (reply + " subscriber(s).\n")));
          console.log("sample_value changed to " + randomNum);
          console.log("last_update changed to " + new_date + '\n');
       });

    changeStatus();
  }, randomTime);
}




