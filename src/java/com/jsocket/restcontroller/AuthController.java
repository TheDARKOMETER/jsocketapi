/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.restcontroller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PathVariable;
import com.jsocket.models.LoginRequest;
import com.jsocket.models.LoginResponse;
import com.jsocket.models.SignUpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

/**
 *
 * @author thebe
 */
@RestController
public class AuthController {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserRepository repository = UserRepository.getInstance();
    Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/user/signup")
    LoginResponse newUser(@RequestBody SignUpRequest newUser) {
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setRole("ROLE_USER");
        user.setUsername(newUser.getUsername());
        user.setCreatedAt(System.currentTimeMillis());
        repository.save(user);
        // Authenticate after signup for convenience :)
        LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        return login(loginRequest);
    }

    @PostMapping("user/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // we create empty context to avoid race condition
        securityContext.setAuthentication(authenticationResponse);
        SecurityContextHolder.setContext(securityContext);
        User user = (User) authenticationResponse.getPrincipal();
        LoginResponse response = new LoginResponse(user.getUsername(), user.getId(), user.getEmail(), user.getRole());
        return response;
    }

    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("/user/{id}")
    User replaceUser(@RequestBody User replaceUserr, @PathVariable Long id) {
        User user = repository.findById(id);
        user.setUsername(replaceUserr.getUsername());
        if (user != null) {
            return repository.save(user);
        } else {
            return repository.save(replaceUserr);
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
