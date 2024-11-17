package com.registrationService.rgService.controllers;


import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {



    @GetMapping("/name")
    @PreAuthorize("hasRole('ROLE_Seller')")
    public String getName(){
        return "Nikhil";
    }

    @PreAuthorize("hasRole('ROLE_Buyer')")
    @GetMapping("/age")
    public int getAge(){
        return 10;
    }

    @PreAuthorize("hasRole('ROLE_Buyer') or " +
            "hasRole('ROLE_Seller')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> loadDashboard(@Valid EntityModel<?> entityModel){
        String message ="load Dashboard";
        EntityModel<?> entityModel1 = EntityModel.of(message);
        return ResponseEntity.ok(message);
    }
}
