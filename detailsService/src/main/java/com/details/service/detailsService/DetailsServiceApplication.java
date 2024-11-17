package com.details.service.detailsService;

import com.details.service.detailsService.entities.Product;
import com.details.service.detailsService.repository.ProductRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DetailsServiceApplication {

//	@Autowired
//	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(DetailsServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner initDatabase() {
//		return args -> {
//			Faker faker = new Faker();
//			List<Product> products = new ArrayList<>();
//
//			for (int i = 1; i <= 100; i++) {
//				Product product = new Product();
//				product.setProductId(UUID.randomUUID().toString());
//				product.setCustomerID(faker.idNumber().valid());
//				product.setProductName(faker.commerce().productName());
//				product.setProductCategory(faker.commerce().department());
//				product.setQuantityAvaliable((long) faker.number().numberBetween(1, 100));
//				product.setProductPrize((long) faker.number().numberBetween(50, 1000));
//				product.setIsAvaliable(faker.bool().bool());
//				product.setProductRegistrationDate(LocalDate.now().minusDays(faker.number().numberBetween(1, 100)));
//				product.setImageUrl(faker.internet().url());
//
//				products.add(product);
//			}
//
//			productRepository.saveAll(products);
//			System.out.println("100 dummy products have been added to MongoDB.");
//		};
//	}
}


