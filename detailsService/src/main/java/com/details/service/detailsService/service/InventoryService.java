package com.details.service.detailsService.service;


import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.repository.ProductRepository;
import com.details.service.detailsService.request.ProductPriceUpdateRequest;
import com.details.service.detailsService.request.ProductQuantityAvailableRequest;
import org.springframework.expression.ExpressionException;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    public Product updateProductQuantity(ProductPriceUpdateRequest productPriceUpdateRequest) throws Exception {
       Product product =  productRepository.findByProductId(
               productPriceUpdateRequest.getProductId())
               .orElseThrow(()->new Exception("Product is not avaliable"));
       product.setProductPrize(Long.valueOf(productPriceUpdateRequest.getPrice()));
       product.setQuantityAvaliable(Long.valueOf(productPriceUpdateRequest.getQuantity()));
       productRepository.save(product);
       return product;
    }

    public Product updateProductAvliability(ProductQuantityAvailableRequest productQuantityAvailableRequest) throws Exception {
        Product product =  productRepository.findByProductId(
                        productQuantityAvailableRequest.getProductId())
                .orElseThrow(()->new Exception("Product is not avaliable"));
        product.setIsAvaliable(productQuantityAvailableRequest.getIsAvailable());
        productRepository.save(product);
        return product;
    }
}
