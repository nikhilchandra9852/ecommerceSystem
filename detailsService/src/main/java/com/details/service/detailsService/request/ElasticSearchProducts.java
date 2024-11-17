package com.details.service.detailsService.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;



@Document(indexName = "products")
public class ElasticSearchProducts {

    @Id
    private String id;


    @Field(type = FieldType.Text, name="productName")
    private String productName;

    @Field(type = FieldType.Text, name="productCategory")
    private String productCategory;

    public ElasticSearchProducts(String id, String productName, String productCategory) {
        this.id = id;
        this.productName = productName;
        this.productCategory = productCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
