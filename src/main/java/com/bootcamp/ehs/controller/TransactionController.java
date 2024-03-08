package com.bootcamp.ehs.controller;

import com.bootcamp.ehs.DTO.TransferDTO;
import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.model.WireTransfer;
import com.bootcamp.ehs.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController   {

    private final ITransactionService transactionService;

    private final ITransactionDepositService depositoService;

    private final ITransactionWithdrawalService withdrawalService;

    private final ITransactionTransferService transferService;

    private final ICommisionService commisionService;

    // Metodo para crear la transaccion
    @PostMapping
    public Mono<ResponseEntity<Transaction>> saveTransaction(@RequestBody Transaction transaction){
        log.info("Procesando transaccion en msTransaction");
        return transactionService.register(transaction)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public Flux<Transaction> findTransactionByAccountId(@PathVariable("accountId") String accountId){
        return transactionService.findTransactionsByAccountId(accountId);
    }

    @PostMapping("/deposit")
    public Mono<Transaction> registerDeposit(@RequestBody Transaction transaction) {
        return depositoService.doDeposit(transaction);
    }

    @PostMapping("/withdrawal")
    public Mono<Transaction> registerWithdrawal(@RequestBody Transaction transaction){
        return withdrawalService.doWithdrawal(transaction);
    }

    @PostMapping("/transfer")
    public Mono<WireTransfer> registerTransferBetweenAccount(@RequestBody TransferDTO transferDTO){
        return transferService.doTransferBetweenAccounts(transferDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
