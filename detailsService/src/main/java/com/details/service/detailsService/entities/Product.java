package com.details.service.detailsService.entities;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document(collection = "ProductDetails")
public class Product {


    @Id
    private String productId;

    @NotBlank
    @Size(max=40)
    private String customerID;

    @NotBlank
    private String productName;

    @NotBlank
    private String productCategory;

    @NotBlank
    @NotNegative
    private Long quantityAvaliable;


    @NotBlank
    @NotNegative
    private Long productPrize;


    @NotBlank
    private Boolean isAvaliable;

    @NotBlank
    private LocalDate productRegistrationDate;

    @NotBlank
    private String imageUrl;
}




@Constraint(validatedBy = NotNegativeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@interface NotNegative {
    String message() default "Value must not be negative";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class NotNegativeValidator implements ConstraintValidator<NotNegative, Number> {

    @Override
    public void initialize(NotNegative constraintAnnotation) {
        // TODO document why this method is empty
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {

        return value == null || value.doubleValue() >= 0;
    }
}
