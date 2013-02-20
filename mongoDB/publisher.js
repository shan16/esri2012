#!/usr/bin/env node

// This script plays the role of publisher.

var mongo = require('mongodb'),
  Server = mongo.Server,
  Db = mongo.Db;

var server = new Server('localhost', 27017, {auto_reconnect: true});
var db = new Db('mydb', server);


require('sugar');

var 
  redis = require("redis"),
  client = redis.createClient();
 
// Generate random channels for different publishers, only for testing.
var randomMachine = Math.floor(Math.random()*4 + 1);
var channelName = "QA900" + randomMachine.toString();

// Publish after "ready"
client.on("ready", function(){
  

db.open(function(err, db) {
  if(!err) {
    console.log(channelName + " connected to mydb.");
    db.collection('machines', function(err, collection) {
      collection.find().toArray(function(err, items){
          

        function changeStatus(){
          var randomTime = Math.floor(Math.random()*(10000-1000+1)+1000);
          var randomNumUsers = Math.floor(Math.random()*100);
          setTimeout(function(){
            //pick random key/value, send to server, stdout
            console.log(items[randomMachine-1]);

            collection.update({key: items[randomMachine-1].key}, {$set: {num_users: randomNumUsers}});

            // change last_update time
            var new_date = (new Date()).toString();
            collection.update({key: items[randomMachine-1].key}, {$set: {last_update: new_date}});

            var message = "num_users changed to " + randomNumUsers + ', '+ 
                "last_update changed to " + new_date + ', ' +
                (0).upto(1).map(function(i) { return "item " + i; }).join(',');

            client.publish(channelName, JSON.stringify({
              message: message
            }), function (err, reply) {

              console.log("From: [" + channelName + "]");
              console.log("Published message to " +
                (reply === 0 ? "no one\n" : (reply + " subscriber(s).\n")));
               });

            changeStatus();
          }, randomTime);
        }

        changeStatus();
       

        client.publish(channelName, JSON.stringify({
          message: items[randomMachine-1]      
        }),function(err, reply){           
             console.log("Initial status published!");
           });
      });
    });
  }
});



/*
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
*/
});

