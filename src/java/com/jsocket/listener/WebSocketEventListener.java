/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.listener;

/**
 *
 * @author User
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import com.jsocket.classes.Greeting;
import com.jsocket.classes.HelloMessage;

@Component
public class WebSocketEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        // Send a greeting to the /topic/greetings destination
        Greeting greeting = new Greeting("Hello, new client!");
        messagingTemplate.convertAndSend("/topic/greetings", greeting);
    }
}