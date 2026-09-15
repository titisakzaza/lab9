package com.example.lab9.service;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositTransaction;
import com.example.lab9.repository.AccountRepository;
import com.example.lab9.repository.DepositRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositService {

    private final AccountRepository accountRepository;
    private final DepositRepository depositRepository;

    public DepositService(AccountRepository accountRepository,
                          DepositRepository depositRepository) {
        this.accountRepository = accountRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public void deposit(Long accountId, Double amount) {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found: " + accountId));

        double currentBalance = account.getBalance() == null ? 0.0 : account.getBalance();
        account.setBalance(currentBalance + amount);
        accountRepository.save(account);

        DepositTransaction deposit = new DepositTransaction();
        deposit.setAmount(amount);
        deposit.setAccount(account);
        depositRepository.save(deposit);

        // ใช้สำหรับข้อทดลอง Rollback เท่านั้น:
        // throw new RuntimeException("Test Rollback");
    }
}
