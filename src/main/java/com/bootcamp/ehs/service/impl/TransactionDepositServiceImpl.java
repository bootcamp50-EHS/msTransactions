package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.service.ICommisionService;
import com.bootcamp.ehs.service.ITransactionDepositService;
import com.bootcamp.ehs.service.ITransactionService;
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
public class TransactionDepositServiceImpl implements ITransactionDepositService {

    private final ITransactionRepo transactionRepo;
    private final IWebClientAccountService accountService;

    private final ICommisionService commisionService;


    @Override
    public Mono<Transaction> doDeposit(Transaction transaction) {
        log.info("Procesando Deposito");
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("El importe de la transacción debe ser mayor a 0"));
        }

        return accountService.findAccountById(transaction.getAccountId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La cuenta ingresada no existe")))
                .flatMap(account -> {
                    System.out.println(account);
                    log.info("Cuenta:", account);
                    account.setAmount(account.getAmount().add(transaction.getAmount()));
                    return accountService.updateAccount(account)
                            .doOnNext(accountUpdate -> log.info("Cuenta Bancaria Actualizada"))
                            .flatMap(accountUpdate -> {
                                transaction.setTypeTransaction("Deposito");
                                transaction.setSign(1);
                                //0transaction.setDateTimeTransaction(LocalDateTime.now());
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
