package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/atm")
public class AtmController {

    @Autowired
    private AtmService atmService;

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String pin) {
        return atmService.login(userId, pin);
    }

    @GetMapping("/balance/{accountNumber}")
    public String checkBalance(@PathVariable String accountNumber) {
        return atmService.checkBalance(accountNumber);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        return atmService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                           @RequestParam double amount,
                           @RequestParam String userId) {
        return atmService.withdraw(accountNumber, amount, userId);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                           @RequestParam String toAccount,
                           @RequestParam double amount,
                           @RequestParam String userId) {
        return atmService.transfer(fromAccount, toAccount, amount, userId);
    }

    @PutMapping("/change-pin")
    public String changePin(@RequestParam String userId,
                            @RequestParam String oldPin,
                            @RequestParam String newPin) {
        return atmService.changePin(userId, oldPin, newPin);
    }

    @GetMapping("/history/{accountNumber}")
    public String getHistory(@PathVariable String accountNumber) {
        return atmService.getTransactionHistory(accountNumber);
    }

    @GetMapping("/loan/{userId}")
    public String checkLoan(@PathVariable String userId) {
        return atmService.checkLoanEligibility(userId);
    }

    @GetMapping("/admin/accounts")
    public List<Account> getAllAccounts() {
        return atmService.getAllAccounts();
    }

    @GetMapping("/accounts/{userId}")
    public List<Account> getUserAccounts(@PathVariable String userId) {
        return atmService.getUserAccounts(userId);
    }

    @GetMapping("/admin/stats")
    public String showStats() {
        atmService.showBankStats();
        return "Stats printed in console!";
    }

    @GetMapping("/health")
    public String health() {
        return "ATM is running!";
    }
}