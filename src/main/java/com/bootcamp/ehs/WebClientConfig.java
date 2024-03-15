package com.bootcamp.ehs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${account.service.url}")
    private String accountServiceUrl;

    @Value("${credit.service.url}")
    private String creditServiceUrl;

    @Bean
    public WebClient accountWebClient() {
        return WebClient.builder()
                .baseUrl(accountServiceUrl)
                .build();
    }

    @Bean
    public WebClient creditWebClient(){
        return WebClient.builder()
                .baseUrl(creditServiceUrl)
                .build();
    }

}
