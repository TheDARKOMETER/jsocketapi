/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;

import com.jsocket.models.ChatMessage;
import com.jsocket.models.Greeting;
import com.jsocket.models.HelloMessage;
import com.jsocket.models.User;
import com.jsocket.repository.ChatMessageRepository;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;

import java.util.UUID;
import java.util.logging.Level;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.util.HtmlUtils;
import org.springframework.util.MultiValueMap;

@Controller
public class MessageController {

    Logger logger = Logger.getLogger(MessageController.class.getName());
    SimpMessagingTemplate template;
    User serverUser = new User("Server", 0, "server@jsocket.com");
    ChatMessageRepository repository = ChatMessageRepository.getInstance();

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    // Message mapping for inbound client messages
    @MessageMapping("/chat")
    @SendTo("/topic/greetings")
    private Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        logger.log(Level.INFO, "MessageController greeting() called with message as {0}", message.getName());
        return new Greeting("Hello, " + message.getName() + "! From STOMP server");
    }

    @MessageMapping("/globalchat")
    @SendTo("/topic/globalchat")
    private ChatMessage onChatMessage(ChatMessage chatMessage) throws Exception {
        System.out.println("onChatMessage called");
        logger.log(Level.INFO, "onChatMessage triggered");
        repository.save(chatMessage);
        return chatMessage;
    }

    // Externally used methods to send messages upon events since these messages will be sent by server not client
    public void sendJoinGreeting(Principal principal) throws Exception {
        String msg = "You have joined the chat";
        logger.log(Level.INFO, "MessageController joinGreeting() called with message as " + msg);
        template.convertAndSendToUser(principal.getName(), "/topic/greetings", new ChatMessage(msg, System.currentTimeMillis(), serverUser, 0, 0, UUID.randomUUID()));
    }

    public void sendGreetingMessage(String usermame) throws Exception {
        logger.log(Level.INFO, "MessageController serverGreetingMessage() called with.");
        ChatMessage serverMsg = new ChatMessage(usermame + " has joined", System.currentTimeMillis(), serverUser, 0, 0, UUID.randomUUID());
        //template.convertAndSend("/topic/globalchat", serverMsg);
        // Save only, sendMessageHistory will do the sending to prevent duplicates. Only fix I can think of for now.
        repository.save(serverMsg);
    }

    public void sendMessageHistory(Principal principal) throws Exception {
        ArrayList<ChatMessage> messageHistory = new ArrayList<ChatMessage>(repository.findAll());
        logger.info("Message history attempting to send to destination /queue/specific-user");
        template.convertAndSendToUser(principal.getName(), "/queue/specific-user", messageHistory);
    }
}
