package com.details.service.detailsService.config;


import com.details.service.detailsService.service.ElasticSearchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticSearchSchedular {

    public ElasticSearchService elasticSearchService;


    public ElasticSearchSchedular(ElasticSearchService elasticSearchService){
        this.elasticSearchService = elasticSearchService;
    }



//    @Scheduled(cron = "0 1/1 * * * *")
    public void updateElasticSearch(){
        try {
            elasticSearchService.updateTheProductNamesIntoElasticSearch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
