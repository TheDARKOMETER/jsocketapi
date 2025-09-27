/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.restcontroller;

import com.jsocket.authentication.AuthService;
import com.jsocket.exceptions.UserNotFoundException;
import com.jsocket.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;
import com.jsocket.models.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author thebe
 */
@RestController
public class AuthController {

    @Autowired
    AuthService authService;
    
    private final UserRepository repository = UserRepository.getInstance();
    Logger logger = Logger.getLogger(AuthController.class.getName());
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);

    @PostMapping("/user/signup")
    User newUser(@RequestBody User newUser) {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        return repository.save(newUser);
    }

    @PostMapping("/user/login")
    User loginUser(@RequestBody User requestUser) throws UserNotFoundException {
        return authService.login(requestUser.getUsername(), requestUser.getPassword());
    }

    @GetMapping("/user/debug")
    public String debug() {
        return "Test";
    }

    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("/user/{id}")
    User replaceUser(@RequestBody User replaceEmployee, @PathVariable Long id) {
        User user = repository.findById(id);
        user.setUsername(replaceEmployee.getUsername());
        if (user != null) {
            return repository.save(user);
        } else {
            return repository.save(replaceEmployee);
        }
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable Long id) {
        try {
            repository.deleteById(id);
        } catch (UserNotFoundException e) {
            logger.log(Level.WARNING, "User not found exception for deleteUser method");
        }
    }

}
