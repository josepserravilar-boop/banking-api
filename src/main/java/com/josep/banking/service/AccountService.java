package com.josep.banking.service;

import com.josep.banking.model.Account;
import com.josep.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> deposit(Long id, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isEmpty()) {
            return Optional.empty();
        }

        Account account = optionalAccount.get();

        account.setBalance(
                account.getBalance().add(amount)
        );

        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> withdraw(Long id, BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <=0){
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isEmpty()) {
            return Optional.empty();
        }
        Account account = optionalAccount.get();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        return Optional.of(accountRepository.save(account));
    }
}
