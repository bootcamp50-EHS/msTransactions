package com.bootcamp.ehs.service;

import com.bootcamp.ehs.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<Transaction> register(Transaction transaction);

    Flux<Transaction> findTransactionsByAccountId(String accountId);


}
