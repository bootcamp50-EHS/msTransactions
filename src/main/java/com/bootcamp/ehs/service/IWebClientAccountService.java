package com.bootcamp.ehs.service;

import com.bootcamp.ehs.DTO.AccountDTO;
import reactor.core.publisher.Mono;

public interface IWebClientAccountService {

    Mono<AccountDTO> findAccountById(String accountId);

    Mono<AccountDTO> updateAccount(AccountDTO account);

}
