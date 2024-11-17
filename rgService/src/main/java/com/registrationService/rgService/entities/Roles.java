package com.registrationService.rgService.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="Roles")
public class Roles {

    @Id
    @Column(name="role_id")
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private UserType role;
}
