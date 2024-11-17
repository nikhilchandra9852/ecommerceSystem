package com.registrationService.rgService.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "login_id")
    private String id;

//    @EmbeddedId
//    private CompositeKey compositeKey;

    @Column(name = "username",nullable = false,unique = true,length = 255)
    private String username;


    @Column(name = "password_hash",nullable = false,length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

}


//@Data
//@Embeddable
//class CompositeKey implements Serializable {
//    String id1;
//    String id2;
//}
