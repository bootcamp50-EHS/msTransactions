package com.bootcamp.ehs.service;

import com.bootcamp.ehs.model.Transaction;
import reactor.core.publisher.Mono;

public interface ICommisionService {

    Mono<Transaction> applyCommision(Transaction transaction);
}
