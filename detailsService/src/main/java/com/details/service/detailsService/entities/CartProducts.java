package com.details.service.detailsService.entities;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Data
@Builder
@Document(collection = "CartProduct")
public class CartProducts {

    @Id
    private String userId;


    @NotNull
    private List<Product> productsList;
}
