package com.registrationService.rgService.controllers;

import java.util.Objects;

import com.registrationService.rgService.entities.Roles;
import com.registrationService.rgService.entities.User;
import com.registrationService.rgService.entities.UserType;
import com.registrationService.rgService.repositories.UserRepository;
import com.registrationService.rgService.repositories.RolesRepository;
import com.registrationService.rgService.configuration.jwtUtils.JwtImpl;
import com.registrationService.rgService.requests.LoginRequest;
import com.registrationService.rgService.requests.MessageResponse;
import com.registrationService.rgService.requests.UserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/v1/auth") // Base URL for authentication-related endpoints
public class RegisterController {

	@Autowired
	AuthenticationManager authenticationManager; // Handles user authentication

	@Autowired
	UserRepository userRepository; // Repository for user-related database operations

	@Autowired
	RolesRepository rolesRepository; // Repository for role-related database operations

	@Autowired
	PasswordEncoder encoder; // Encoder for password hashing

	@Autowired
	JwtImpl jwtUtils; // Utility for generating JWT tokens

	/**
	 * Register a new user account.
	 *
	 * @param userRequest The signup request containing user details.
	 * @return A ResponseEntity indicating success or error message.
	 */
	@PostMapping("/signup")
	public EntityModel<ResponseEntity<?>> registerUser(@Valid @RequestBody UserRequest userRequest) {

		// Check if the username is already taken
		if (Boolean.TRUE.equals(userRepository.existsByUsername(userRequest.getUserName()))) {
			return getResponseEntityEntityModel(ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!")));
		}

		// Check if the email is already in use
		if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
			return getResponseEntityEntityModel(ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!")));
		}

		// Create a new user's account
		User user = new User(
				userRequest.getUserName(),
				userRequest.getEmail(),
				encoder.encode(userRequest.getPassword()),
				userRequest.getPhoneNumber()); // Encode the password

		UserType strRoles = userRequest.getUserType(); // Get the roles from the request
		Roles roles = null; // Initialize a set to hold the user roles

		// Assign roles based on the request or default to user role

        if (Objects.requireNonNull(strRoles) == UserType.Seller) {
            roles = rolesRepository.findByRole(UserType.Seller)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            roles = rolesRepository.findByRole(UserType.Buyer)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }


		// Assign roles to the user and save it to the database
		user.setUserType(roles.getRole());
		userRepository.save(user);
		ResponseEntity<MessageResponse> ok = ResponseEntity.ok(new MessageResponse("User registered successfully!"));

		EntityModel<ResponseEntity<?>> responseEntityEntityModel = EntityModel.of(ok);

		Link link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(LoginController.class).authenticateUser(new LoginRequest())

		).withSelfRel();
		responseEntityEntityModel.add(link);


		// Return a success message upon successful registration
		return responseEntityEntityModel;
	}

	private static EntityModel<ResponseEntity<?>> getResponseEntityEntityModel(ResponseEntity<MessageResponse> body) {
		EntityModel<ResponseEntity<?>> responseEntityEntityModel = EntityModel.of(body);

		Link link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RegisterController.class).registerUser(new UserRequest())

		).withSelfRel();
		responseEntityEntityModel.add(link);
		return responseEntityEntityModel;
	}
}
