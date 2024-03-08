package com.bootcamp.ehs.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICommissionStrategy {

    Mono<BigDecimal> calculateCommission(Long transactionCount);
}
