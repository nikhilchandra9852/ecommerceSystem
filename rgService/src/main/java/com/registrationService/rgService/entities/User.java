package com.registrationService.rgService.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_name")
    @NotNull
    private String username;

    @Column(name = "email")
    @Email
    private String email;
    @Column(name="hash_password")
    private String hashPassword;

    @Column(name = "phone_number")
    @NumberFormat
    private String phoneNumber;

    @Column(name = "registration_date")
    @DateTimeFormat
    private LocalDate registrationDate;

    @Column(name = "user_type")
    private UserType userType;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addressList;

    @OneToMany(mappedBy ="user",cascade = CascadeType.REFRESH)
    private List<Login> loginList;

    public User(String username, String email, String password,String phoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.hashPassword = password;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
        this.addressList = new ArrayList<>();
        this.loginList =new ArrayList<>();
    }

}
