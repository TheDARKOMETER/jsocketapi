/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;

import com.jsocket.models.ChatMessage;
import com.jsocket.models.Greeting;
import com.jsocket.models.HelloMessage;
import com.jsocket.models.User;
import com.jsocket.models.UsersOnline;
import com.jsocket.repository.ChatMessageRepository;
import com.jsocket.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.ArrayList;

import java.util.UUID;
import java.util.logging.Level;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.util.HtmlUtils;
import org.springframework.util.MultiValueMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    
    
    Logger logger = Logger.getLogger(MessageController.class.getName());
    SimpMessagingTemplate template;
    ChatMessageRepository chatRepository = ChatMessageRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();
    User serverUser = userRepository.findByEmail("javachat@jchat.com");
    UsersOnline usersOnline = new UsersOnline();
    
    
    private void getOnlineUserDetails() {
        usersOnline.setOnlineCount(simpUserRegistry.getUserCount());
        List<String> onlineUsers = simpUserRegistry.getUsers().stream().map(user -> user.getName()).collect(Collectors.toList());
        usersOnline.setUserList((ArrayList<String>) onlineUsers);
    }
    
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
        Long userId = chatMessage.getAuthor().getId();
        User existingUser = userRepository.findById(userId);
        
        if (existingUser == null) {
            throw new RuntimeException("User with ID" + userId  + "not found");
        }
        
        chatMessage.setAuthor(existingUser);
        chatRepository.save(chatMessage);
        return chatMessage;
    }
    
    @MessageMapping("/guest")
    public void handleRequest(Principal principal) throws Exception {
        sendGuestUser(principal);
    }

    // Externally used methods to send messages upon events since these messages will be sent by server not client
    public void sendJoinGreeting(Principal principal) throws Exception {
        String msg = "You have joined the chat";
        logger.log(Level.INFO, "MessageController joinGreeting() called with message as " + msg);
        template.convertAndSendToUser(principal.getName(), "/topic/greetings", new ChatMessage(msg, System.currentTimeMillis(), serverUser, 0, 0, UUID.randomUUID()));
    }

    public void sendGreetingMessage(String usermame) throws Exception {
        logger.log(Level.INFO, "MessageController serverGreetingMessage() called with.");
        ChatMessage serverMsg = new ChatMessage("You have connected", System.currentTimeMillis(), serverUser, 0, 0, UUID.randomUUID());
        //template.convertAndSend("/topic/globalchat", serverMsg);
        // Save only, sendMessageHistory will do the sending to prevent duplicates. Only fix I can think of for now.
        chatRepository.save(serverMsg);
    }

    public void sendMessageHistory(Principal principal) throws Exception {
        ArrayList<ChatMessage> messageHistory = new ArrayList<ChatMessage>(chatRepository.findAll());
        logger.info("Message history attempting to send to destination /queue/specific-user");
        template.convertAndSendToUser(principal.getName(), "/queue/specific-user", messageHistory);
    }

    public void sendGuestUser(Principal principal) throws Exception {
        logger.info("Attempting to find user by username " + "Guest-" + principal.getName());
        User user = userRepository.findByUsername("Guest-" + principal.getName());
        logger.info("Found user: " + user.getUsername());
        template.convertAndSendToUser(principal.getName(), "/queue/guest-user", user);
    }
    
    public void broadcastOnlineUsers() {
        getOnlineUserDetails();
        template.convertAndSend("/topic/online-users", usersOnline);
    }
}
