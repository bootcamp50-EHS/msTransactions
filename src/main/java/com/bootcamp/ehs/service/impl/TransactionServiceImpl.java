package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepo transactionRepo;
    @Override
    public Mono<Transaction> register(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    @Override
    public Flux<Transaction> findTransactionsByAccountId(String accountId) {
        return transactionRepo.findAllByAccountId(accountId);
    }
}
