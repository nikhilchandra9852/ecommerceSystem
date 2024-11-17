package com.details.service.detailsService.controller;


import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.request.ProductPriceUpdateRequest;
import com.details.service.detailsService.request.ProductQuantityAvailableRequest;
import com.details.service.detailsService.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }


    @PatchMapping("/update/value")
    public EntityModel<Product> updateProductQuantity(@Valid @RequestBody ProductPriceUpdateRequest productPriceUpdateRequest) throws Exception {
        EntityModel<Product> model = EntityModel.of(inventoryService.updateProductQuantity(productPriceUpdateRequest));
        Link link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class).getProduct(productPriceUpdateRequest.getProductId())
        ).withSelfRel();
        model.add(link);
        return model;
    }

    @PatchMapping("/update/status")
    public EntityModel<Product> updateProductStatus(@Valid @RequestBody ProductQuantityAvailableRequest productQuantityAvailableRequest) throws Exception {
        EntityModel<Product> model = EntityModel.of(inventoryService.updateProductAvliability(productQuantityAvailableRequest));
        Link link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class).getProduct(productQuantityAvailableRequest.getProductId())
        ).withSelfRel();
        model.add(link);
        return model;
    }

}
