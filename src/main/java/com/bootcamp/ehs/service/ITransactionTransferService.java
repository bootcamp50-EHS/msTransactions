package com.bootcamp.ehs.service;

import com.bootcamp.ehs.DTO.TransferDTO;
import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.model.WireTransfer;
import reactor.core.publisher.Mono;

public interface ITransactionTransferService {

    Mono<WireTransfer> doTransferBetweenAccounts(TransferDTO transfer);
}
