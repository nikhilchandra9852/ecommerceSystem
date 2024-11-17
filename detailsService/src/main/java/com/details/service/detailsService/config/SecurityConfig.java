package com.details.service.detailsService.config;


import com.details.service.detailsService.interceptors.ProductInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/v1/inventory/**").hasRole("Seller")
                .requestMatchers("/api/v1/product/**").hasRole("Seller")
                .requestMatchers("/api/v1/searchProducts/**").hasRole("Buyer")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new ProductInterceptor(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
