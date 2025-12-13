/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.restcontroller;

import com.jsocket.dispatcherconfig.UserPrincipal;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;

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

    @Autowired
    private SecurityContextRepository securityContextRepository;

    private final UserRepository repository = UserRepository.getInstance();
    Logger logger = Logger.getLogger(AuthController.class.getName());

    // Checking security filter for /admin path
    @GetMapping("/admin/debug")
    String debug() {
        return "Debug";
    }

    // Checking security filter for /user path
    @GetMapping("/user/debug")
    String debugUser() {
        return "Debug";
    }

    @PostMapping("/user/signup")
    public ResponseEntity<String> newUser(@RequestBody SignUpRequest newUser) {
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        logger.log(Level.INFO, "New user with email: " + newUser.getEmail());
        try {
            user.setEmail(newUser.getEmail());
            user.setRole("ROLE_USER");
            user.setUsername(newUser.getUsername());
            user.setCreatedAt(System.currentTimeMillis());
            User savedUser = repository.save(user);
            logger.log(Level.INFO, "Saving user with id: " + savedUser.getId());
            logger.log(Level.INFO, "Saving user with email: " + savedUser.getEmail());
            // Authenticate after signup for convenience :)
            LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
            return ResponseEntity.ok("Sign up success");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email or username already exists");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "error signing up user", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occured");
        }
    }

    @PostMapping("/user/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // we create empty context to avoid race condition
        securityContext.setAuthentication(authenticationResponse);
        SecurityContextHolder.setContext(securityContext);
        UserPrincipal user = (UserPrincipal) authenticationResponse.getPrincipal();
        logger.log(Level.INFO, "Returning user with id: " + user.getId());
        logger.log(Level.INFO, "Returning user with email: " + user.getEmail());
        securityContextRepository.saveContext(securityContext, request, response);
        LoginResponse loginResponse = new LoginResponse(user.getUsername(), user.getId(), user.getEmail(), user.getRole());
        return loginResponse;
    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("/user/{id}")
    User replaceUser(@RequestBody User replaceUser, @PathVariable Long id) {
        User user = repository.findById(id);
        user.setUsername(replaceUser.getUsername());
        if (user != null) {
            return repository.save(user);
        } else {
            return repository.save(replaceUser);
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
