package com.registrationService.rgService.controllers;


import com.registrationService.rgService.configuration.jwtUtils.JwtImpl;
import com.registrationService.rgService.requests.JwtResponse;
import com.registrationService.rgService.requests.LoginRequest;
import com.registrationService.rgService.service.LoginService;
import com.registrationService.rgService.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {


    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    public LoginService loginService;


    @Autowired
    JwtImpl jwtUtils; // Utility for generating JWT tokens

    @Autowired
    AuthenticationManager authenticationManager; // Handles user authentication


    /**
     * Authenticate user and return a JWT token if successful.
     *
     * @param loginRequest The login request containing username and password.
     * @return A ResponseEntity containing the JWT response or an error message.
     */
    @PostMapping("/signin")
    public EntityModel<ResponseEntity<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate the user with the provided username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(),
                        loginRequest.getPassword()));

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Get user details from the authentication object
        UserService userDetails = (UserService) authentication.getPrincipal();

        // Extract user roles into a list
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Generate JWT token based on the authentication
        String jwt = jwtUtils.generateJwtToken(authentication,roles);


        // Return a response containing the JWT and user details


        ResponseEntity<JwtResponse> ok = ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));

        // sending the dash board link
        EntityModel<ResponseEntity<?>> responseEntityEntityModel = EntityModel.of(ok);

        Link link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TestController.class).loadDashboard(responseEntityEntityModel)

        ).withSelfRel();
        responseEntityEntityModel.add(link);

        return responseEntityEntityModel;
    }


}
