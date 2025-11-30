/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;

import com.jsocket.repository.UserRepository;

import com.jsocket.models.User;
/**
 *
 * @author thebe
 */

   

@RestController
public class JSocketRestController {
    
    private final UserRepository userRepository = UserRepository.getInstance();
    
    
    // UNUSED
    // TODO: Login user once signup user is finished
//    @PostMapping("/users")
//    public User loginUser(@RequestBody User user) {
//        return new User
////    }
//    
//    @PostMapping("/signup") 
//    public User signupUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }
//    
//      
    
}
