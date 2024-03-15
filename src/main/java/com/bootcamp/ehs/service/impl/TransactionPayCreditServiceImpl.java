package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.service.ITransactionPayCreditService;
import com.bootcamp.ehs.service.IWebClientAccountService;
import com.bootcamp.ehs.service.IWebClientCreditService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionPayCreditServiceImpl implements ITransactionPayCreditService {

    private final ITransactionRepo transactionRepo;
    private final IWebClientCreditService creditService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionPayCreditServiceImpl.class);

    @Override
    public Mono<Transaction> doPayCredit(Transaction transaction) {
        LOGGER.info("Transactions -> doPayCredit: Procesando Pago de Crédito");
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("Transactions -> doPayCredit: El importe  debe ser mayor a 0"));
        }

        return creditService.findCreditById(transaction.getCreditId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transactions -> doPayCredit: La cuenta ingresada no existe")))
                .doOnNext(credit -> LOGGER.info("El credito: {}",credit) )
                .filter(credit -> credit.getAmount().compareTo(credit.getPayment().add(transaction.getAmount())) >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transactions -> doPayCredit: El pago es mayor al credito")))
                .flatMap(credit -> {
                    System.out.println(credit);
                    LOGGER.info("Credito: {}", credit);
                    credit.setPayment(credit.getPayment().add(transaction.getAmount()));
                    return creditService.updateCredit(credit)
                            .doOnNext(accountUpdate -> LOGGER.info("Transactions -> doPayCredit: Cuenta Bancaria Actualizada"))
                            .flatMap(accountUpdate -> {
                                transaction.setTypeTransaction("Pago Credito");
                                transaction.setSign(1);
                                transaction.setDateTimeTransaction(LocalDateTime.now());
                                return transactionRepo.save(transaction);
                            });

                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Transactions -> doPayCredit: Error en pago de credito: " + e.getMessage(), e));
                });

    }
}
