/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import com.jsocket.models.User;
/**
 *
 * @author thebe
 */

@RestController
public class JSocketRestController {
    
    @PostMapping("/users")
    public User loginUser(@RequestBody User user) {
        
    }
    
}
