package com.details.service.detailsService.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductQuantityAvailableRequest {

    @NotNull
    private String productId;

    @NotNull
    private Boolean isAvailable;
}
