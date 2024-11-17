package com.details.service.detailsService.entities;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
@Document(collection = "orderDetails")
public class Orders {

    @Id
    private String id;

    @NotBlank
    @Size(max = 40)
    private String sellerId;

    @NotBlank
    @Size(max = 40)
    private String customerId;

    @NotBlank
    @Size(max = 40)
    private String productId;

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotNull
    private Long quantityOrdered;

    @NotNull
    private Long pricePerUnit;

    @NotNull
    private Long totalPrice;

    @NotNull
    private LocalDate orderDate;

    @NotBlank
    private String orderStatus;

    @Size(max = 500)
    private String shippingAddress;

    @NotNull
    private Boolean isShipped;

    private LocalDate shipmentDate;

    @Size(max = 40)
    private String trackingNumber;
}
