package com.details.service.detailsService.repository;

import com.details.service.detailsService.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByProductName(String productName);

    Optional<Product> findByProductId(String productID);

}
