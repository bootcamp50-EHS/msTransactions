package com.bootcamp.ehs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {

    private String codeOperation;
    private String description;
    private String dateTransfer;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
}
