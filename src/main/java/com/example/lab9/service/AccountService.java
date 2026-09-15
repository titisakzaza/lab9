package com.example.lab9.service;

import com.example.lab9.model.Account;
import com.example.lab9.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
    }
}
