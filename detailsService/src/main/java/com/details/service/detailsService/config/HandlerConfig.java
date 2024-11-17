package com.details.service.detailsService.config;


import com.details.service.detailsService.interceptors.ProductInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HandlerConfig implements WebMvcConfigurer {


    @Autowired
    public ProductInterceptor productInterceptor;


    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(productInterceptor).excludePathPatterns("/auth/getToken/**").addPathPatterns("/**");

    }
}
