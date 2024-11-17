package com.details.service.detailsService.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration {

    @Bean
    public RestClient getRestClient(){
        return  RestClient.builder(
                new HttpHost("localhost",9200)
        ).build();
    }

    // create one more bean for elasticSearchTransport
    // transport object helps to automatically map our Model class
    // to Json and integrated with Api Client.
    @Bean
    public ElasticsearchTransport getElasticSearchTransport(){
        return new RestClientTransport(
                getRestClient(),new JacksonJsonpMapper()
        );
    }


    // bean for ElasticClient
    @Bean
    public ElasticsearchClient getElasticSearchClient(){
        return  new ElasticsearchClient(getElasticSearchTransport());
    }



}
