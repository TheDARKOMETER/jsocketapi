package com.jsocket.dispatcherconfig;

import com.jsocket.models.User;
import com.jsocket.repository.UserRepository;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    Logger logger = Logger.getLogger(CustomHandshakeHandler.class.getName());

    @Override
    protected Principal determineUser(ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        HttpHeaders headers = request.getHeaders();
        headers.forEach((key, value) -> logger.info(key + "Headerkey: " + value));

        List<String> cookies = headers.get("Cookie");
        if (cookies != null) {
            logger.info("Handshake cookies: " + cookies);
        } else {
            logger.info("No cookies found in handshake headers");
        }

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object securityContext = attributes.get("SPRING_SECURITY_CONTEXT");

        if (securityContext != null
                && securityContext instanceof org.springframework.security.core.context.SecurityContext) {

            org.springframework.security.core.context.SecurityContext context = (org.springframework.security.core.context.SecurityContext) securityContext;
            Authentication auth = context.getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                logger.info(">>> User is logged in" + (UserDetails) auth.getPrincipal());
                return () -> ((UserDetails) auth.getPrincipal()).getUsername();
            }

        }
        return createGuestPrincipal(request);
    }

    public Principal createGuestPrincipal(ServerHttpRequest request) {
        URI uri = request.getURI();
        String query = uri.getQuery();
        String userId = UUID.randomUUID().toString();
        Map<String, String> queryParams = Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(c -> c[0], c -> c[1]));
        logger.info("Query params: " + queryParams.toString());

        if ("false".equals(queryParams.get("isLoggedin"))) {
            logger.info("is logged in is false, saving guest user");
            UserRepository userRepository = UserRepository.getInstance();
            User guestUser = new User("Guest-" + userId, System.currentTimeMillis(), null);
            guestUser.setRole("ROLE_GUEST");
            User savedGuestUser = userRepository.save(guestUser);
            logger.info(savedGuestUser.getUsername() + " successfully saved");
            return new Principal() {
                @Override
                public String getName() {
                    return guestUser.getUsername();
                }
            };
        } else {
            logger.info("is logged in is true, no need to save guest user");
        }
        // Generate a unique user identifier (you can use session ID, UUID, etc.)
        logger.info("userId: " + userId);
        // Return custom Principal with that ID
        return new Principal() {
            @Override
            public String getName() {
                return userId;
            }
        };
    }
}
