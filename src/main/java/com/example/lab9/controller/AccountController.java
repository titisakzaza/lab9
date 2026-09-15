package com.example.lab9.controller;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositRequest;
import com.example.lab9.service.AccountService;
import com.example.lab9.service.DepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final DepositService depositService;

    public AccountController(AccountService accountService,
                             DepositService depositService) {
        this.accountService = accountService;
        this.depositService = depositService;
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
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Map<String, String>> deposit(
            @PathVariable Long id,
            @RequestBody DepositRequest request) {

        depositService.deposit(id, request.getAmount());

        return ResponseEntity.ok(
                Map.of("message", "Deposit successful")
        );
    }

    @GetMapping("/{id}/deposit")
    public ResponseEntity<Map<String, String>> depositInstructions(
            @PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
                "message", "Use POST to deposit money into this account",
                "endpoint", "/accounts/" + id + "/deposit",
                "body", "{\"amount\": 1000}"
        ));
    }
}
