package com.registrationService.rgService.requests;

import com.registrationService.rgService.entities.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {

    private  String id;

    @NotNull
    private String userName;

    @Email
    private String email;
    @NotNull
    private String password;

    @NumberFormat
    private String phoneNumber;

    @NotNull
    private UserType userType;


    private LocalDate registrationDate;


}
