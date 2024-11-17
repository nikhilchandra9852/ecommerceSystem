package com.details.service.detailsService.controller;


import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.request.ElasticSearchProducts;
import com.details.service.detailsService.request.ProductRequest;
import com.details.service.detailsService.service.ElasticSearchService;
import com.details.service.detailsService.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {


    private ProductService productService;

    private ElasticSearchService elasticSearchService;
    private List<Object> theProducts;

    public ProductController(ProductService service,ElasticSearchService elasticSearchService){

        this.productService =service;
        this.elasticSearchService = elasticSearchService;
    }

    @PostMapping("/addProduct")
    public EntityModel<?> addProduct(@Valid @RequestBody ProductRequest productRequest){

        Product content = productService.addProductToCustomer(productRequest);
        EntityModel<?> model = EntityModel.of(content);

        Link link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class).getProduct(content.getProductId())
        ).withSelfRel();
        model.add(link);
        return model;
    }

    @GetMapping("/getProduct")
    public EntityModel<Product> getProduct(@Valid @RequestParam("id") String id) {

        Product product = productService.getProductId(id);
        return EntityModel.of(product);
    }

    @GetMapping("/allProducts")
    public EntityModel<List<Product>> allProducts(){
        return EntityModel.of(productService.getAllProducts());
    }




}
