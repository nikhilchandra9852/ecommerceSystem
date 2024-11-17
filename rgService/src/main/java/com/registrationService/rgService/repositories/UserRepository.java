package com.registrationService.rgService.repositories;

import com.registrationService.rgService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    /**
     * Find a User by their username.
     *
     * @param username The username of the user.
     * @return An Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);
    /**
     * Find a User by their email.
     *
     * @param email The username of the user.
     * @return An Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);


    /**
     * Check if a username already exists in the database.
     *
     * @param username The username to check.
     * @return A Boolean indicating whether the username exists (true) or not (false).
     */
    Boolean existsByUsername(String username);

    /**
     * Check if an email already exists in the database.
     *
     * @param email The email to check.
     * @return A Boolean indicating whether the email exists (true) or not (false).
     */
    Boolean existsByEmail(String email);

}
