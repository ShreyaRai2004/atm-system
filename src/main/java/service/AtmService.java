package com.atm.service;

import com.atm.model.Account;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AtmService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> fraudAttempts = new ConcurrentHashMap<>();

    public AtmService() {
        accounts.put("ACC001", new Account("ACC001", "USER001", 50000, "1234"));
        accounts.put("ACC002", new Account("ACC002", "USER001", 25000, "1234"));
        accounts.put("ACC003", new Account("ACC003", "USER002", 75000, "5678"));
        System.out.println("Loaded " + accounts.size() + " accounts");
        showBankStats();
    }

    private void sendNotification(String userId, String message) {
        System.out.println("=========================================");
        System.out.println("NOTIFICATION to " + userId + ":");
        System.out.println(message);
        System.out.println("Time: " + LocalDateTime.now());
        System.out.println("=========================================");
    }

    private void detectFraud(String userId, String accountNumber, double amount, String action) {
        if (action.equals("WITHDRAW")) {
            fraudAttempts.put(userId, fraudAttempts.getOrDefault(userId, 0) + 1);

            if (fraudAttempts.get(userId) > 3) {
                System.out.println("\n===== FRAUD ALERT ===== ");
                System.out.println("User: " + userId);
                System.out.println("Suspicious: Multiple withdrawals detected!");
                System.out.println("Account: " + accountNumber);
                System.out.println("Amount: Rs." + amount);
                System.out.println("=========================\n");
            }
        }

        Account acc = accounts.get(accountNumber);
        if (acc != null && amount > acc.getBalance() * 0.5) {
            System.out.println("\n===== FRAUD ALERT ===== ");
            System.out.println("User: " + userId);
            System.out.println("Large withdrawal: " + (amount/acc.getBalance()*100) + "% of balance");
            System.out.println("Account: " + accountNumber);
            System.out.println("Amount: Rs." + amount);
            System.out.println("=========================\n");
        }
    }

    public void showBankStats() {
        double totalBalance = 0;
        int totalAccounts = accounts.size();
        double maxBalance = 0;
        String richestAccount = "";
        double minBalance = Double.MAX_VALUE;
        String poorestAccount = "";

        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            double balance = entry.getValue().getBalance();
            totalBalance += balance;

            if (balance > maxBalance) {
                maxBalance = balance;
                richestAccount = entry.getKey();
            }
            if (balance < minBalance) {
                minBalance = balance;
                poorestAccount = entry.getKey();
            }
        }

        double avgBalance = totalAccounts > 0 ? totalBalance / totalAccounts : 0;

        System.out.println("\n===== BANK STATISTICS ===== ");
        System.out.println("Total Accounts: " + totalAccounts);
        System.out.println("Total Balance: Rs." + totalBalance);
        System.out.println("Average Balance: Rs." + String.format("%.2f", avgBalance));
        System.out.println("Richest Account: " + richestAccount + " (Rs." + maxBalance + ")");
        System.out.println("Poorest Account: " + poorestAccount + " (Rs." + minBalance + ")");
        System.out.println("================================\n");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void applyDailyInterest() {
        System.out.println("\n===== INTEREST APPLIED ===== ");
        System.out.println("Time: " + LocalDateTime.now());

        for (Account acc : accounts.values()) {
            double interestRate = 0.04;
            double dailyInterest = acc.getBalance() * interestRate / 365;
            acc.setBalance(acc.getBalance() + dailyInterest);
            acc.addBalanceHistory(acc.getBalance(), "INTEREST");
            System.out.println("Account: " + acc.getAccountNumber() + " | Rs." +
                    String.format("%.2f", dailyInterest) + " added");
        }
        System.out.println("================================\n");
    }

    public String login(String userId, String pin) {
        for (Account acc : accounts.values()) {
            if (acc.getUserId().equals(userId) && acc.getPin().equals(pin)) {
                sendNotification(userId, "Login successful!");
                return "Login successful! Welcome " + userId;
            }
        }
        return "Invalid User ID or PIN!";
    }

    public String checkBalance(String accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) return "Account not found!";
        return "Balance: Rs." + acc.getBalance();
    }

    public String deposit(String accountNumber, double amount) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) return "Account not found!";
        if (amount <= 0) return "Amount must be positive!";

        acc.setBalance(acc.getBalance() + amount);
        acc.addBalanceHistory(acc.getBalance(), "DEPOSIT");
        acc.addTransaction("DEPOSIT", amount, acc.getBalance());

        sendNotification(acc.getUserId(), "Deposit of Rs." + amount + " successful!");
        showBankStats();

        return "Deposited Rs." + amount + ". New balance: Rs." + acc.getBalance();
    }

    public String withdraw(String accountNumber, double amount, String userId) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) return "Account not found!";
        if (!acc.getUserId().equals(userId)) return "You don't own this account!";
        if (amount <= 0) return "Amount must be positive!";
        if (amount > acc.getBalance()) return "Insufficient balance!";
        if (!acc.canWithdrawToday(amount)) return "Daily limit exceeded! Limit: Rs." + acc.getDailyLimit();

        acc.setBalance(acc.getBalance() - amount);
        acc.setTodayWithdrawn(acc.getTodayWithdrawn() + amount);
        acc.addBalanceHistory(acc.getBalance(), "WITHDRAW");
        acc.addTransaction("WITHDRAW", amount, acc.getBalance());

        detectFraud(userId, accountNumber, amount, "WITHDRAW");
        sendNotification(userId, "Withdrawal of Rs." + amount + " successful!");
        showBankStats();

        return "Withdrew Rs." + amount + ". Remaining: Rs." + acc.getBalance();
    }

    public String transfer(String fromAcc, String toAcc, double amount, String userId) {
        Account from = accounts.get(fromAcc);
        Account to = accounts.get(toAcc);

        if (from == null || to == null) return "Account not found!";
        if (!from.getUserId().equals(userId)) return "You don't own this account!";
        if (amount <= 0) return "Amount must be positive!";
        if (amount > from.getBalance()) return "Insufficient balance!";
        if (fromAcc.equals(toAcc)) return "Cannot transfer to same account!";

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        from.addBalanceHistory(from.getBalance(), "TRANSFER_OUT");
        to.addBalanceHistory(to.getBalance(), "TRANSFER_IN");
        from.addTransaction("TRANSFER_OUT", amount, from.getBalance());
        to.addTransaction("TRANSFER_IN", amount, to.getBalance());

        sendNotification(from.getUserId(), "Transfer of Rs." + amount + " to " + toAcc + " successful!");
        showBankStats();

        return "Transferred Rs." + amount + " from " + fromAcc + " to " + toAcc;
    }

    public String changePin(String userId, String oldPin, String newPin) {
        for (Account acc : accounts.values()) {
            if (acc.getUserId().equals(userId)) {
                if (!acc.getPin().equals(oldPin)) return "Wrong old PIN!";
                if (newPin.length() != 4) return "PIN must be 4 digits!";
                acc.setPin(newPin);
                sendNotification(userId, "PIN changed successfully!");
                return "PIN changed successfully!";
            }
        }
        return "User not found!";
    }

    public void showBalanceHistory(String accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.println("\n===== BALANCE HISTORY ===== ");
        System.out.println("Account: " + accountNumber);
        System.out.println("Current Balance: Rs." + acc.getBalance());
        System.out.println("-----------------------------");

        for (Account.BalanceHistory bh : acc.getLastHistory()) {
            System.out.println(bh.timestamp.substring(0, 19) + " | " +
                    bh.action + " | Rs." + bh.balance);
        }
        System.out.println("================================\n");
    }

    public String getTransactionHistory(String accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) return "Account not found!";

        StringBuilder result = new StringBuilder();
        result.append("\n===== TRANSACTION HISTORY ===== \n");
        result.append("Account: " + accountNumber + "\n");
        result.append("-----------------------------\n");

        for (Account.TransactionRecord tr : acc.getLastTransactions(5)) {
            result.append(tr.timestamp.substring(0, 19) + " | " +
                    tr.type + " | Rs." + tr.amount + "\n");
        }
        result.append("================================\n");
        return result.toString();
    }

    public String checkLoanEligibility(String userId) {
        System.out.println("\n===== LOAN ELIGIBILITY ===== ");
        System.out.println("User: " + userId);

        List<Account> userAccounts = getUserAccounts(userId);
        double totalBalance = 0;

        for (Account acc : userAccounts) {
            totalBalance += acc.getBalance();
        }

        System.out.println("Total Balance: Rs." + totalBalance);

        if (totalBalance > 100000) {
            double loanAmount = totalBalance * 0.5;
            System.out.println("ELIGIBLE for loan!");
            System.out.println("Max Loan Amount: Rs." + loanAmount);
            return "Eligible for Rs." + loanAmount + " loan";
        } else {
            System.out.println("NOT ELIGIBLE");
            System.out.println("Need minimum Rs.100,000 balance");
            return "Not eligible. Need Rs.100,000 balance";
        }
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> getUserAccounts(String userId) {
        List<Account> userAccounts = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc.getUserId().equals(userId)) {
                userAccounts.add(acc);
            }
        }
        return userAccounts;
    }
}