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
import java.security.Principal;
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
        logger.log(Level.INFO, "A client connected (SessionConnectEvent)");
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) throws Exception {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        String sessionId = headerAccessor.getSessionId();
        Principal principal = headerAccessor.getUser();
        String username = headerAccessor.getFirstNativeHeader("username");
        logger.log(Level.INFO, "sha message: {0} dest: {1}, sessionId: {2}, principalname: {3}", new Object[]{headerAccessor.toString(), destination, sessionId, principal.getName()});
        if (destination.equals("/topic/globalchat")) {
            logger.info("Welcoming new global chat subscriber");
            messageController.joinGreeting();
            messageController.serverGreetingMessage(username);
        } else if (destination.equals("/user/queue/specific-user")) {
            logger.info("Destination for user matches, sending history to specific user");
            messageController.sendMessageHistory(principal);
        }

        logger.log(Level.INFO, "A client subscribed, sending history");
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) throws Exception {
        logger.log(Level.INFO, "A client connected (SessionConnectedEvent)");
    }
}
