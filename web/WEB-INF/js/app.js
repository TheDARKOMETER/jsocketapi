/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

let StompJs = require('@stomp/stompjs');

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/jsocketapi'
});


stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/general', function (greeting) {
        showGreeting(JSON.parse(greeting.body).content);
    });
};


 