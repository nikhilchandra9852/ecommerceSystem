package com.details.service.detailsService.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductPriceUpdateRequest {

    @NotNull
    private String  productId;

    @NotNull
    private String  quantity;

    @NotNull
    private String price;


}
