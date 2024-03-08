package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.DTO.AccountDTO;
import com.bootcamp.ehs.DTO.TransferDTO;
import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.model.WireTransfer;
import com.bootcamp.ehs.repo.ITransactionRepo;
import com.bootcamp.ehs.repo.IWireTransferRepo;
import com.bootcamp.ehs.service.ITransactionTransferService;
import com.bootcamp.ehs.service.IWebClientAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionTransferServiceImpl implements ITransactionTransferService {

    private final ITransactionRepo transactionRepo;
    private final IWebClientAccountService accountService;
    private final IWireTransferRepo wireTransferRepo;

    // Metodo que realiza la transferencia entre cuentas
    @Override
    public Mono<WireTransfer> doTransferBetweenAccounts(TransferDTO transfer) {
        return accountService.findAccountById(transfer.getFromAccountId())
                .filter(accountFrom -> accountFrom.getAmount().compareTo(transfer.getAmount()) > 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Fondos insuficientes en la cuenta origen")))
                .flatMap(accountFrom -> accountService.findAccountById(transfer.getToAccountId())
                        .filter(accountTo -> accountFrom.getBank().equals(accountTo.getBank()))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("No puede transferir a bancos diferentes")))
                        .flatMap(accountTo -> processTransfer(accountFrom, accountTo, transfer)));
    }

    private Mono<WireTransfer> processTransfer(AccountDTO accountFrom, AccountDTO accountTo, TransferDTO transfer) {
        updateAccountBalances(accountFrom, accountTo, transfer.getAmount());
        return saveTransactions(accountFrom, accountTo, transfer)
                .then(accountService.updateAccount(accountFrom))
                .then(accountService.updateAccount(accountTo))
                .flatMap(account -> saveWireTransfer(accountFrom, accountTo, transfer));
    }

    private void updateAccountBalances(AccountDTO accountFrom, AccountDTO accountTo, BigDecimal amount) {
        accountFrom.setAmount(accountFrom.getAmount().subtract(amount));
        accountTo.setAmount(accountTo.getAmount().add(amount));
    }

    private Mono<Transaction> saveTransactions(AccountDTO accountFrom, AccountDTO accountTo, TransferDTO transfer) {
        Transaction withdrawal = createTransaction(transfer, "Transferencia", -1, accountFrom.getId());
        Transaction deposit = createTransaction(transfer, "Transferencia", 1, accountTo.getId());
        return transactionRepo.save(withdrawal).then(transactionRepo.save(deposit));
    }

    private Transaction createTransaction(TransferDTO transfer, String type, int sign, String accountId) {
        return new Transaction(null, transfer.getCodeOperation(), transfer.getDescription(), LocalDate.now().toString(),
                transfer.getAmount(), type, sign, accountId, LocalDateTime.now());
    }

    private Mono<WireTransfer> saveWireTransfer(AccountDTO accountFrom, AccountDTO accountTo, TransferDTO transfer) {
        WireTransfer wireTransfer = new WireTransfer();
        wireTransfer.setAccountFrom(accountFrom);
        wireTransfer.setAccountTo(accountTo);
        wireTransfer.setAmount(transfer.getAmount());
        wireTransfer.setDateCreate(LocalDate.now().toString());
        return wireTransferRepo.save(wireTransfer);
    }


}
