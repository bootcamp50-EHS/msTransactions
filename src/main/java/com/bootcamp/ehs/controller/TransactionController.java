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
@RequestMapping("/api/transaction")
public class TransactionController   {

    /**
     * Servicio para Transacciones bancarias.
     */
    private final ITransactionService transactionService;
    /**
     * Servicio para Depositos a cuentas bancarias.
     */
    private final ITransactionDepositService depositoService;
    /**
     * Servicio para Retiros de cuentas bancarias.
     */
    private final ITransactionWithdrawalService withdrawalService;
    /**
     * Servicio para transferencias entre cuentas bancarias.
     */
    private final ITransactionTransferService transferService;
    /**
     * Servicio para Pagos de creditos bancarios.
     */
    private final ITransactionPayCreditService payCreditService;


    /**
     * Método para guardar una transacción.
     *
     * @param transaction La transacción a guardar.
     * @return Una entidad de respuesta que contiene la transacción guardada.
     */
    @PostMapping
    public Mono<ResponseEntity<Transaction>> saveTransaction(@RequestBody final Transaction transaction){
        log.info("Procesando transaccion en msTransaction");
        return transactionService.register(transaction)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Método para encontrar transacciones por ID de cuenta.
     *
     * @param accountId El ID de la cuenta para buscar transacciones.
     * @return Un Flux de transacciones asociadas con el ID de cuenta proporcionado.
     */
    @GetMapping("/account/{accountId}")
    public Flux<Transaction> findTransactionByAccountId(@PathVariable("accountId") String accountId){
        return transactionService.findTransactionsByAccountId(accountId);
    }

    /**
     * Método para registrar un depósito.
     *
     * @param transaction La transacción a depositar.
     * @return Una transacción que representa el depósito.
     */
    @PostMapping("/deposit")
    public Mono<Transaction> registerDeposit(@RequestBody Transaction transaction) {
        return depositoService.doDeposit(transaction);
    }

    /**
     * Método para registrar un retiro.
     *
     * @param transaction La transacción a retirar.
     * @return Una transacción que representa el retiro.
     */
    @PostMapping("/withdrawal")
    public Mono<Transaction> registerWithdrawal(@RequestBody Transaction transaction){
        return withdrawalService.doWithdrawal(transaction);
    }

    /**
     * Método para registrar una transferencia entre cuentas.
     *
     * @param transferDTO El objeto de transferencia para la transferencia.
     * @return Una transferencia que representa la transferencia entre cuentas.
     */
    @PostMapping("/transfer")
    public Mono<WireTransfer> registerTransferBetweenAccount(@RequestBody TransferDTO transferDTO){
        return transferService.doTransferBetweenAccounts(transferDTO);
    }

    @PostMapping("/paycredit")
    public Mono<ResponseEntity<Transaction>> registerPayCredit(@RequestBody Transaction transaction){
        return payCreditService.doPayCredit(transaction).
                map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/paycreditkafka")
    public Mono<ResponseEntity<Transaction>> registerPayCreditWithKafka(@RequestBody Transaction transaction){

        return payCreditService.doPayCreditWithKafka(transaction).
                map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
