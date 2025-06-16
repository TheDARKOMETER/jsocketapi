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
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import com.jsocket.classes.Greeting;
import org.springframework.context.event.EventListener;
import com.jsocket.classes.HelloMessage;
import java.util.logging.Logger;

@Component
public class LegacyWSEventListener implements ApplicationListener<SessionConnectedEvent> {
    Logger logger = Logger.getLogger(WebSocketEventListener.class.getName());
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        // Send a greeting to the /topic/greetings destination
        Greeting greeting = new Greeting("Hello, new client!");
        System.out.println("A client connected");
        
        messagingTemplate.convertAndSend("/topic/greetings", greeting);
    }
   
}