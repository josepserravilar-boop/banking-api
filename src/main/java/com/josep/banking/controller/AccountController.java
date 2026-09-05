package com.josep.banking.controller;

import com.josep.banking.model.Account;
import com.josep.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.josep.banking.dto.AmountRequest;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long id,
            @RequestBody AmountRequest request) {

        return accountService.deposit(id, request.getAmount())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long id,
            @RequestBody AmountRequest request) {

        return accountService.withdraw(id, request.getAmount())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}