package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.DTO.AccountDTO;
import com.bootcamp.ehs.DTO.CreditDTO;
import com.bootcamp.ehs.service.IWebClientCreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientCreditService implements IWebClientCreditService {

    @Qualifier("creditWebClient")
    private final WebClient creditWebClient;

    @Override
    public Mono<CreditDTO> findCreditById(String creditId) {
        return creditWebClient.get()
                .uri("/api/credit/retrieve/{creditId}", creditId)
                .retrieve()
                .bodyToMono(CreditDTO.class);
    }

    @Override
    public Mono<CreditDTO> updateCredit(CreditDTO credit) {
        return creditWebClient.put()
                .uri("/api/credit/update")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(credit)
                .retrieve()
                .bodyToMono(CreditDTO.class);
    }
}
