package com.bootcamp.ehs.service.impl;

import com.bootcamp.ehs.service.ICommissionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
public class FixedCommissioStrategy implements ICommissionStrategy {

    // metodo para calcular una comision Fija (10),
    @Override
    public Mono<BigDecimal> calculateCommission(Long transactionCount) {
        log.info("En metodo calculateComission"+transactionCount.toString());
        return Mono.fromSupplier(() -> { //creará un Mono que emitira un BigDecimal
            BigDecimal commission = BigDecimal.ZERO;
            if (transactionCount > 10) {
                commission = new BigDecimal("5.00");
            }
            return commission;
        });
    }

}
