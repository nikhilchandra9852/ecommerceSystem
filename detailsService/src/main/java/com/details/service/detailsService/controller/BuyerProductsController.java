package com.details.service.detailsService.controller;

import com.details.service.detailsService.entities.CartProducts;
import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.request.ElasticSearchProducts;
import com.details.service.detailsService.service.CartService;
import com.details.service.detailsService.service.ElasticSearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequestMapping("/api/v1/searchProducts")
@RestController
public class BuyerProductsController {

    private ElasticSearchService elasticSearchService;
    private CartService cartService;

    public BuyerProductsController(ElasticSearchService elasticSearchService){
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/searchProduct/{name}")
    public CollectionModel<ElasticSearchProducts> searchProduct(@PathVariable String name) throws IOException {
        List<ElasticSearchProducts> theProducts = elasticSearchService.getTheProducts(name);

        CollectionModel<ElasticSearchProducts> collectionModel = CollectionModel.of(theProducts);



        theProducts.forEach(object->
                collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getProduct(object.getId())).withSelfRel())
        );
        return collectionModel;
    }


    @PostMapping("/addToCart")
    public EntityModel<CartProducts> addToCarts(HttpServletRequest httpServletRequest, @RequestBody List<ElasticSearchProducts> elasticSearchProducts) throws Exception {
        String userId = (String) httpServletRequest.getAttribute("userId");

        if (userId == null) {
            System.out.println("User ID is not found");
            throw new Exception("UserId is not found from JWt Token");
        }
        EntityModel<CartProducts> cartProductsEntityModel =  EntityModel.of(cartService.saveProductsInCartService(userId,elasticSearchProducts));

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BuyerProductsController.class).getCartList(userId)
        ).withSelfRel();

        cartProductsEntityModel.add(link);
        return cartProductsEntityModel;

    }

    @GetMapping("/getTheCartProducts")
    private @NotNull List<Product> getCartList(@Valid @RequestParam("userId") String userID) {

        return cartService.getProducts(userID);
    }

}
