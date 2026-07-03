package com.atm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String userId;
    private double balance;
    private String pin;
    private double dailyLimit = 100000.0;
    private double todayWithdrawn = 0;
    private String lastWithdrawDate = LocalDate.now().toString();

    private List<BalanceHistory> balanceHistory = new ArrayList<>();
    private List<TransactionRecord> transactions = new ArrayList<>();

    public Account(String accountNumber, String userId, double balance, String pin) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.pin = pin;
        this.balanceHistory.add(new BalanceHistory(balance, "INITIAL"));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    public String getPin() { return pin; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setPin(String pin) { this.pin = pin; }
    public double getDailyLimit() { return dailyLimit; }
    public double getTodayWithdrawn() { return todayWithdrawn; }
    public void setTodayWithdrawn(double todayWithdrawn) { this.todayWithdrawn = todayWithdrawn; }
    public String getLastWithdrawDate() { return lastWithdrawDate; }
    public void setLastWithdrawDate(String lastWithdrawDate) { this.lastWithdrawDate = lastWithdrawDate; }

    public void addBalanceHistory(double balance, String action) {
        balanceHistory.add(new BalanceHistory(balance, action));
    }

    public List<BalanceHistory> getLastHistory() {
        int size = balanceHistory.size();
        int start = Math.max(0, size - 10);
        return balanceHistory.subList(start, size);
    }

    public void addTransaction(String type, double amount, double balanceAfter) {
        transactions.add(new TransactionRecord(type, amount, balanceAfter));
    }

    public List<TransactionRecord> getLastTransactions(int count) {
        int size = transactions.size();
        int start = Math.max(0, size - count);
        return transactions.subList(start, size);
    }

    public boolean canWithdrawToday(double amount) {
        if (!lastWithdrawDate.equals(LocalDate.now().toString())) {
            todayWithdrawn = 0;
            lastWithdrawDate = LocalDate.now().toString();
        }
        return (todayWithdrawn + amount) <= dailyLimit;
    }

    public static class BalanceHistory {
        public double balance;
        public String timestamp;
        public String action;

        public BalanceHistory(double balance, String action) {
            this.balance = balance;
            this.timestamp = LocalDateTime.now().toString();
            this.action = action;
        }
    }

    public static class TransactionRecord {
        public String type;
        public double amount;
        public double balanceAfter;
        public String timestamp;

        public TransactionRecord(String type, double amount, double balanceAfter) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.timestamp = LocalDateTime.now().toString();
        }
    }
}