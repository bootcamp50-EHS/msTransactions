package com.bootcamp.ehs.service;

import com.bootcamp.ehs.DTO.CreditDTO;
import reactor.core.publisher.Mono;

public interface IWebClientCreditService {

    Mono<CreditDTO> findCreditById(String creditId);

    Mono<CreditDTO> updateCredit(CreditDTO credit);
}
