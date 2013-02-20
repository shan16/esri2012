esri_2012
@ author Feng Shan ESRI Summer 2012

This is the repo for the code that I developed during the 2012 summer internship at ESRI Desktop QA team.

SCV folder contains the code for the SVC test package project that I worked with Byron. This is a automation tool for the developers to run tests. Since I could not post the actual working version of code, I post the proof-of-concept version of the basic idea. This project was developed in Java based on the existing C# version. It loads up the ExampleDLL file and generate the checkers on the GUI.

socket, redis, mongoDB, dnode, combo folders contain the code for the lab machine management project I worked with Roy Tinker. Each of those is a proof-of-concept of the work I have done for the lab management tool. redis contains publish/subscribe method for a lab machine to send information. mongoDB is the server side database to store the information sent by lab machines. combo is the combination of redis and mongoDB. socket contains the javascript files for the browsers to show the results in real-time online.