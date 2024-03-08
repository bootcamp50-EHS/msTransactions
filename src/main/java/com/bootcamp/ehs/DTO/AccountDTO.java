package com.bootcamp.ehs.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String id;
    private BankDTO bank;
    private BigDecimal amount ;
    private Integer numberTransactions ;

}
