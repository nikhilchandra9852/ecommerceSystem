package com.details.service.detailsService.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.details.service.detailsService.repository.ProductRepository;
import com.details.service.detailsService.request.ElasticSearchProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticSearchService {

    private final Logger logger = LoggerFactory.getLogger(ElasticSearchProducts.class);

    public ProductRepository productRepository;
    private final static String indexName ="products";

    private  ElasticsearchClient elasticsearchClient;
    private List<Object> objectList;


    public ElasticSearchService(ProductRepository productRepository,ElasticsearchClient elasticsearchClient){
        this.productRepository = productRepository;
        this.elasticsearchClient =elasticsearchClient;
    }


    public void updateTheProductNamesIntoElasticSearch() throws IOException {
        List<ElasticSearchProducts> productList = productRepository.findAll()
                .stream()
                .map(e -> new ElasticSearchProducts(e.getProductId(), e.getProductName(),e.getProductCategory()))
                .toList();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q.matchAll(m -> m))
        );
        SearchResponse<Map> searchResponse = elasticsearchClient.search(
                searchRequest, Map.class);


        Set<String> existingProductIds = searchResponse.hits().hits().stream()
                .map(hit -> hit.source().get("id").toString())
                .collect(Collectors.toSet());


        List<ElasticSearchProducts> productsToIndex = productList.stream()
                .filter(product -> !existingProductIds.contains(product.getId()))
                .toList();


        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();
        productsToIndex.forEach(product -> bulkRequest.operations(op -> op
                .index(idx -> idx.index(indexName)
                        .id(product.getId())
                        .document(product))));

        if (!productsToIndex.isEmpty()) {
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest.build());

            if (bulkResponse.errors()) {
                logger.error("Bulk indexing encountered errors.");
                bulkResponse.items().forEach(item -> {
                    if (item.error() != null) {
                        logger.error("Error indexing product ID {}: {}", item.id() ,item.error().reason());
                    }
                });
            } else {
                logger.info("Successfully indexed {} products into Elasticsearch.", productsToIndex.size());
            }
        } else {
            logger.info("All products are already up to date in Elasticsearch.");
        }
    }

    // need to do the orders for the sellers and

    public List<ElasticSearchProducts> getTheProducts(String name) throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s ->
                s.index(indexName)
                        .query(q -> q
                                .bool(b -> b.should(f -> f.fuzzy(fz -> fz
                                                .field("productName")
                                                .value(name)
                                                .fuzziness("AUTO")
                                        ))
                                        .should(f -> f.fuzzy(fz -> fz
                                                .field("productCategory")
                                                .value(name)
                                                .fuzziness("AUTO")
                                        ))
                                        .should(w -> w
                                                .wildcard(wc -> wc.field("productName")
                                                        .value("*" + name + "*")))
                                        .should(w -> w
                                                .wildcard(wc -> wc.field("productCategory")
                                                        .value("*" + name + "*")))

                        ))
        );
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest,Map.class);
        List<Hit> list = searchResponse.hits().hits().stream().map(e->e).toList();

        return list.stream().map(e->(LinkedHashMap)e.source())
                .map(e->new ElasticSearchProducts(e.get("id")
                        .toString(),e.get("productName")
                        .toString(),e.get("productCategory")
                        .toString())).toList();

    }
}

