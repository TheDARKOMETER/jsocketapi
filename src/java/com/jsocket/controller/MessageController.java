/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;

import com.jsocket.models.ChatMessage;
import com.jsocket.models.Greeting;
import com.jsocket.models.HelloMessage;
import java.util.logging.Level;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    Logger logger = Logger.getLogger(MessageController.class.getName());
    SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        logger.log(Level.INFO, "MessageController greeting() called with message as {0}", message.getName());
        return new Greeting("Hello, " + message.getName() + "! From STOMP server");
    }

    @MessageMapping("/globalchat")
    @SendTo("/topic/globalchat")
    public ChatMessage onChatMessage(ChatMessage chatMessage)  throws Exception  {
        System.out.println("onCHatMessage called");
        logger.log(Level.INFO, "onChatMessage triggered");
        return chatMessage;
    }

    public void joinGreeting() throws Exception {
        Thread.sleep(1000); // simulated delay
        logger.log(Level.INFO, "MessageController joinGreeting() called with message as");
        template.convertAndSend("/topic/greetings", new Greeting("Greetings to globalchat"));
    }
}
