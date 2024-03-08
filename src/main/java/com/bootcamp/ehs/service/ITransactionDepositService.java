package com.bootcamp.ehs.service;

import com.bootcamp.ehs.model.Transaction;
import reactor.core.publisher.Mono;

public interface ITransactionDepositService {

    Mono<Transaction> doDeposit(Transaction transaction);

}
