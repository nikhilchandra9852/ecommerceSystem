package com.details.service.detailsService.service;


import com.details.service.detailsService.entities.CartProducts;
import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.repository.CartRepository;
import com.details.service.detailsService.repository.ProductRepository;
import com.details.service.detailsService.request.ElasticSearchProducts;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private CartRepository cartServiceRepository;
    private ProductRepository productRepository;

    private EntityManager entityManager;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       EntityManager  entityManager
                       ){
        this.cartServiceRepository  =cartRepository;
        this.productRepository =productRepository;
        this.entityManager =  entityManager;
    }


    @Transactional(rollbackFor = Exception.class )
    public CartProducts saveProductsInCartService(String userId, List<ElasticSearchProducts> elasticSearchProductsList){
        registerUserId(userId);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<String> productIds = elasticSearchProductsList.stream().map(ElasticSearchProducts::getId).toList();
        List<Product> listProducts = new ArrayList<>();
        // update the quantitis in the product
        elasticSearchProductsList.forEach( elasticSearchProducts -> {
            // Create a CriteriaUpdate for each product
            CriteriaUpdate<Product> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Product.class);
            Root<Product> productRoot = criteriaUpdate.from(Product.class);

            criteriaUpdate.set("quantity",criteriaBuilder.diff(productRoot.get("quantity"),1));
            if(Integer.parseInt(productRoot.get("quantity").toString())<2){
                criteriaUpdate.set("isAvaliable",Boolean.FALSE);
            }
            entityManager.createQuery(criteriaUpdate).executeUpdate();

            Product updatedProduct = entityManager.find(Product.class, elasticSearchProducts.getId());
            if (updatedProduct != null) {
                listProducts.add(updatedProduct);
            }
        });
        // check if the userId present or not
        CartProducts build = CartProducts.builder().userId(userId).productsList(listProducts).build();
        cartServiceRepository.save(build);
        return build;

    }

    private void registerUserId(String userId) {

        cartServiceRepository.findById(userId).orElse(
            cartServiceRepository.save(CartProducts.builder().userId(userId).build())
        );
    }

    public @NotNull List<Product> getProducts(String userID) {
        CartProducts cartProducts = cartServiceRepository.findById(userID).orElseThrow();
        return  cartProducts.getProductsList();
    }
}
