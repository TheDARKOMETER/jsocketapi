/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;

import com.jsocket.classes.Greeting;
import com.jsocket.classes.HelloMessage;
import java.util.logging.Level;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.logging.Logger;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    Logger logger = Logger.getLogger(MessageController.class.getName());

    @MessageMapping("/chat")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        logger.log(Level.INFO, "MessageController called with message as " + message.getName());
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
