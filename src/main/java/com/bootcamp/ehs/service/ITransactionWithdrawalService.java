package com.bootcamp.ehs.service;

import com.bootcamp.ehs.model.Transaction;
import reactor.core.publisher.Mono;

public interface ITransactionWithdrawalService {

    Mono<Transaction> doWithdrawal(Transaction transaction);
}
