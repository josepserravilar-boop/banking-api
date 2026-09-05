package com.josep.banking.controller;

import com.josep.banking.model.Transaction;
import com.josep.banking.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsByAccountId(
            @PathVariable Long accountId) {

        return transactionService.getTransactionsByAccountId(accountId);
    }
}