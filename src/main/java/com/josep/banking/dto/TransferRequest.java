package com.josep.banking.dto;

import java.math.BigDecimal;

public class TransferRequest {

    private Long destinationAccountId;
    private BigDecimal amount;

    public TransferRequest() {
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}