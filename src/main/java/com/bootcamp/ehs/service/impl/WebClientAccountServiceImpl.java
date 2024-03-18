package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.DTO.AccountDTO;
import com.bootcamp.ehs.service.IWebClientAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientAccountServiceImpl implements IWebClientAccountService {

    @Qualifier("accountWebClient")
    private final WebClient accountWebClient;

    @Qualifier("gatewayServiceUrl")
    private final WebClient webClient;

    @Override
    public Mono<AccountDTO> findAccountById(String accountId) {
        return webClient.get()
                .uri("/api/account/list/{accountId}", accountId)
                .retrieve()
                .bodyToMono(AccountDTO.class);
    }

    @Override
    public Mono<AccountDTO> updateAccount(AccountDTO account) {
        return webClient.put()
                .uri("/api/account/update")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(account)
                .retrieve()
                .bodyToMono(AccountDTO.class);
    }


}
