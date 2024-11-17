package com.registrationService.rgService.repositories;

import com.registrationService.rgService.entities.Roles;
import com.registrationService.rgService.entities.UserType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RolesRepository extends CrudRepository<Roles, String> {

    /**
     * Find a Role by its name.
     *
     * @param name The name of the role represented as an UserType enum.
     * @return An Optional containing the Role if found, or empty if not found.
     */
    Optional<Roles> findByRole(UserType name);
}

