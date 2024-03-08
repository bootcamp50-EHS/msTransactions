package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.service.ICommissionStrategy;
import com.bootcamp.ehs.service.ICommisionService;
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
public class CommissionServiceImpl implements ICommisionService {

    private final ITransactionRepo transactionRepo;
    private final ICommissionStrategy commissionStrategy;
    private final IWebClientAccountService accountService;
    @Override
    public Mono<Transaction> applyCommision(Transaction transaction) {
        log.info("Procesando la Comision");
        //LocalDateTime now = LocalDateTime.now();
        // Calcular la fecha de inicio del mes
        //LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        //return transactionRepo.findBy
        // AccountIdAndTimestampBetween(transaction.getAccountId(), startOfMonth, now)
        return transactionRepo.countByAccountId(transaction.getAccountId())
                .count()
                .flatMap(count -> commissionStrategy.calculateCommission(count))
                .filter(monto -> monto.compareTo(BigDecimal.ZERO) > 0)
                .doOnNext(cuenta -> log.info("Pasando a logica de comision: "+ cuenta))
                .flatMap(commission -> accountService.findAccountById(transaction.getAccountId())
                        .flatMap(account -> {
                            System.out.println(commission);
                            account.setAmount(account.getAmount().subtract(commission));
                            return accountService.updateAccount(account)
                                    .flatMap(accountUpdate -> {
                                        Transaction commissionTransaction = new Transaction();
                                        commissionTransaction.setCodeOperation(transaction.getCodeOperation());
                                        commissionTransaction.setAccountId(account.getId());
                                        commissionTransaction.setAmount(commission);
                                        commissionTransaction.setDescription("Comision bancaria");
                                        commissionTransaction.setTypeTransaction("Comision");
                                        commissionTransaction.setSign(-1);
                                        commissionTransaction.setDateTimeTransaction(LocalDateTime.now());
                                        return transactionRepo.save(commissionTransaction);
                                    });
                        }));
    }
}
