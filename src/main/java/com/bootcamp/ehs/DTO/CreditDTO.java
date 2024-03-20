package com.bootcamp.ehs.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDTO {

    private String id;
    private BankDTO bank;
    private String customerId;
    private String creditType;
    private BigDecimal amount;
    private BigDecimal payment ;
}
