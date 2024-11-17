package com.details.service.detailsService.service;

import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.repository.ProductRepository;
import com.details.service.detailsService.request.ProductRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {


    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


    public Product addProductToCustomer(ProductRequest productRequest) {

            Product product  = new Product();
            product.setCustomerID(productRequest.getCustomerID());
            product.setProductId(productRequest.getProductId());
            product.setProductName(productRequest.getProductName());
            product.setProductCategory(productRequest.getProductCategory());
            product.setQuantityAvaliable(productRequest.getQuantityAvaliable());
            product.setProductPrize(productRequest.getProductPrize());
            product.setIsAvaliable(isProductAvaliable(productRequest.getQuantityAvaliable()));
            product.setProductRegistrationDate(LocalDate.now());
            product.setImageUrl("Image Url");
            productRepository.save(product);
            return product;
    }

    private Boolean isProductAvaliable(Long quantityAvaliable) {
        return quantityAvaliable>0?Boolean.TRUE:Boolean.FALSE;
    }

    public Product getProductId(String productID){
        return productRepository.findByProductId(productID).orElse(null);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
