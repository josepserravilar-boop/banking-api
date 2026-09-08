package com.josep.banking.service;
import com.josep.banking.model.Transaction;
import com.josep.banking.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import com.josep.banking.model.Account;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);

        accountService = new AccountService(
                accountRepository,
                transactionRepository
        );
    }
    @Test
    void depositShouldIncreaseBalance() {

        Account account = new Account(
                "Josep",
                "josep@example.com",
                new BigDecimal("1000.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Account> result =
                accountService.deposit(
                        1L,
                        new BigDecimal("100.00")
                );

        assertEquals(
                new BigDecimal("1100.00"),
                result.get().getBalance()
        );

        verify(transactionRepository)
                .save(any(Transaction.class));
    }

    @Test
    void depositShouldRejectNegativeAmount() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(
                        1L,
                        new BigDecimal("-100.00")
                )
        );
    }

    @Test
    void withdrawShouldDecreaseBalance() {

        Account account = new Account(
                "Josep",
                "josep@example.com",
                new BigDecimal("1000.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Account> result =
                accountService.withdraw(
                        1L,
                        new BigDecimal("200.00")
                );

        assertEquals(
                new BigDecimal("800.00"),
                result.get().getBalance()
        );

        verify(transactionRepository)
                .save(any(Transaction.class));
    }

    @Test
    void withdrawShouldRejectInsufficientBalance() {

        Account account = new Account(
                "Josep",
                "josep@example.com",
                new BigDecimal("100.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.withdraw(
                        1L,
                        new BigDecimal("200.00")
                )
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }

    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        Account sourceAccount = new Account(
                "Josep",
                "josep@example.com",
                new BigDecimal("1000.00")
        );

        Account destinationAccount = new Account(
                "Maria",
                "maria@example.com",
                new BigDecimal("500.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(destinationAccount));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Account> result =
                accountService.transfer(
                        1L,
                        2L,
                        new BigDecimal("300.00")
                );

        assertEquals(
                new BigDecimal("700.00"),
                sourceAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("800.00"),
                destinationAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("700.00"),
                result.get().getBalance()
        );

        verify(transactionRepository)
                .save(any(Transaction.class));
    }

    @Test
    void transferShouldRejectInsufficientBalance() {

        Account sourceAccount = new Account(
                "Josep",
                "josep@example.com",
                new BigDecimal("100.00")
        );

        Account destinationAccount = new Account(
                "Maria",
                "maria@example.com",
                new BigDecimal("500.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(destinationAccount));

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transfer(
                        1L,
                        2L,
                        new BigDecimal("300.00")
                )
        );

        assertEquals(
                new BigDecimal("100.00"),
                sourceAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("500.00"),
                destinationAccount.getBalance()
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }
}