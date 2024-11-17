package com.details.service.detailsService.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductRequest {

    @JsonIgnore
    private String productId = UUID.randomUUID().toString();

    @NotNull
    private String customerID;

    @NotNull
    private String productName;

    @NotNull
    private String productCategory;

    @NotNull
    private Long quantityAvaliable;

    @NotNull
    private Long productPrize;

    @JsonIgnore
    private Boolean isAvaliable;

    @JsonIgnore
    private LocalDate productRegistrationDate;

    @JsonIgnore
    private MultipartFile multipartFile;

    @JsonIgnore
    private String fileUrl;
}
