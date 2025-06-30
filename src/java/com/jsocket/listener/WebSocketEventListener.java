package com.jsocket.listener;

import com.jsocket.controller.MessageController;
import com.jsocket.models.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import com.jsocket.models.ChatMessage;
import java.util.logging.Level;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import java.util.logging.Logger;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

    Logger logger = Logger.getLogger(WebSocketEventListener.class.getName());

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageController messageController;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("A client connected (SessionConnectEvent)");
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) throws Exception {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        String sessionId = headerAccessor.getSessionId();
        logger.log(Level.INFO, "sha message: {0} dest: {1}, sessionId: {2}", new Object[]{headerAccessor.toString(), destination, sessionId});
        if (destination.equals("/topic/globalchat")) {
            logger.info("Welcoming new global chat subscriber");
            messageController.joinGreeting();
        }
        
        System.out.println("A client subscribed");
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) throws Exception {
        System.out.println("A client connected (SessionConnectedEvent)");
    }
}
