package com.josep.banking.service;

import com.josep.banking.model.*;
import com.josep.banking.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(
                "DEPOSIT",
                null,
                id,
                amount
        );

        transactionRepository.save(transaction);

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

        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(
                "WITHDRAW",
                id,
                null,
                amount
        );

        transactionRepository.save(transaction);

        return Optional.of(accountRepository.save(account));
    }

    @Transactional
    public Optional<Account> transfer(
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero"
            );
        }

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }

        Optional<Account> optionalSourceAccount =
                accountRepository.findById(sourceAccountId);

        Optional<Account> optionalDestinationAccount =
                accountRepository.findById(destinationAccountId);

        if (optionalSourceAccount.isEmpty() ||
                optionalDestinationAccount.isEmpty()) {
            return Optional.empty();
        }

        Account sourceAccount = optionalSourceAccount.get();
        Account destinationAccount = optionalDestinationAccount.get();

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(amount)
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance().add(amount)
        );

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction(
                "TRANSFER",
                sourceAccountId,
                destinationAccountId,
                amount
        );

        transactionRepository.save(transaction);

        return Optional.of(sourceAccount);
    }

}
