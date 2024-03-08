package com.bootcamp.ehs.model;

import com.bootcamp.ehs.DTO.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "WireTransfers")
public class WireTransfer {

    private String id;
    private AccountDTO accountFrom;
    private AccountDTO accountTo;
    private String dateCreate;
    private BigDecimal amount;
}
