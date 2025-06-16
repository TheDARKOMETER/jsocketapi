package com.jsocket.listener;

import com.jsocket.classes.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("A client connected (SessionConnectEvent)");
        messagingTemplate.convertAndSend("/topic/greetings", new Greeting("Hello, new client!"));
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        System.out.println("A client connected (SessionConnectedEvent)");
    }
}
