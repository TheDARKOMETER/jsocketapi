/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsocket.dispatcherconfig;

import com.jsocket.dispatcherconfig.UserPrincipal;
import com.jsocket.dispatcherconfig.UserPrincipal;
import com.jsocket.dispatcherconfig.UserPrincipal;
import com.jsocket.models.User;
import com.jsocket.repository.UserRepository;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author thebe
 */
@Service
public class JSocketUserDetailService implements UserDetailsService {

    private UserRepository userRepository = UserRepository.getInstance();
    private Logger logger = Logger.getLogger(JSocketUserDetailService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info(">>> JSocketUserDetailService called with username: " + username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.info(">>> User not found in DB");
            throw new UsernameNotFoundException("User not found: " + username);
        }
        logger.info(">>> Found user in DB: " + user.getUsername());

        return new UserPrincipal(user);
    }
}
