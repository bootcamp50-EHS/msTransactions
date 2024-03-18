package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.service.ICommisionService;
import com.bootcamp.ehs.service.ITransactionWithdrawalService;
import com.bootcamp.ehs.service.IWebClientAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionWithdrawalServiceImpl implements ITransactionWithdrawalService {

    private final ITransactionRepo transactionRepo;
    private final IWebClientAccountService accountService;
    private final ICommisionService commisionService;

    @Override
    public Mono<Transaction> doWithdrawal(Transaction transaction) {
        log.info("Procesando Retiro");
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("El importe de la transacción debe ser mayor a 0"));
        }

        return accountService.findAccountById(transaction.getAccountId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La cuenta ingresada no existe")))
                .filter(account -> account.getAmount().compareTo(transaction.getAmount()) >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Saldo insuficiente para realizar el retiro")))
                .flatMap(account -> {
                    account.setAmount(account.getAmount().subtract(transaction.getAmount()));
                    return accountService.updateAccount(account)
                            .doOnNext(accountUpdate -> log.info("Cuenta Bancaria Actualizada"))
                            .flatMap(accountUpdate -> {
                                transaction.setTypeTransaction("Retiro");
                                transaction.setSign(-1);
                                return transactionRepo.save(transaction)
                                        .then(commisionService.applyCommision(transaction))
                                        .thenReturn(transaction);
                            });

                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Ocurrió un error al registrar el depósito: " + e.getMessage(), e));
                });
    }
}
