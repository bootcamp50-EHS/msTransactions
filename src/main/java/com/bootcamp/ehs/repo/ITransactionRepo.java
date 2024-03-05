package com.bootcamp.ehs.repo;

import com.bootcamp.ehs.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ITransactionRepo extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findAllByAccountId(String accountId);

}
