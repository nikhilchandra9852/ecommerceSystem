package com.registrationService.rgService.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class LoginRequest {


    @JsonIgnore
    private String Id;

    @NotNull
    @Email
    private String emailId;

    @NotNull
    private String password;

    @NotNull
    private String roleType;

    @JsonIgnore
    private LocalDate loginDate;
}
