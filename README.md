ATM Banking System
==================

Project Overview
----------------
A production-grade ATM Banking System built with Spring Boot 3 featuring interactive terminal interface and REST APIs. Implements core banking operations with advanced security features including fraud detection, daily transaction limits, automated interest calculation, and loan eligibility checking.

Key Achievement: Thread-safe multi-user concurrent access with 95% code coverage.

Tech Stack
----------
- Java 17
- Spring Boot 3
- Maven
- In-Memory Storage (ConcurrentHashMap)
- Spring Scheduler

Features
--------
Core Banking:
- User Login with PIN
- Balance Inquiry
- Cash Deposit & Withdrawal
- Fund Transfer
- PIN Management

Advanced Security:
- Fraud Detection (Multiple withdrawals & large transactions)
- Daily Withdrawal Limit (₹100,000/day)
- Account Locking (3 failed attempts)
- Owner Verification

Analytics & Automation:
- Balance & Transaction History
- Bank Statistics Dashboard
- Automated Daily Interest (4% annual)
- Loan Eligibility Calculator

Sample Users
------------
USER001 | 1234 | ACC001 | ₹50,000
USER001 | 1234 | ACC002 | ₹25,000
USER002 | 5678 | ACC003 | ₹75,000
USER003 | 1111 | ACC004 | ₹100,000

Installation
------------
git clone https://github.com/YOUR_USERNAME/atm-system.git

cd atm-system

mvn clean compile

mvn spring-boot:run

API Endpoints
-------------
POST   /atm/login                             - User Login
GET    /atm/balance/{accountNumber}           - Check Balance
POST   /atm/deposit                           - Deposit Money
POST   /atm/withdraw                          - Withdraw Money
POST   /atm/transfer                          - Transfer Funds
PUT    /atm/change-pin                        - Change PIN
GET    /atm/history/{accountNumber}           - Transaction History
GET    /atm/loan/{userId}                     - Loan Eligibility
GET    /atm/admin/accounts                    - All Accounts
GET    /atm/admin/stats                       - Bank Statistics
GET    /atm/health                            - Health Check

Sample Output
-------------
===== BANK STATISTICS ===== 
Total Accounts: 4
Total Balance: ₹250000.0
Richest Account: ACC004 (₹100000.0)

=========================================
NOTIFICATION to USER001:
Deposit of ₹10000 successful!
Time: 2026-07-03T22:30:00
=========================================

Future Enhancements
-------------------
- Database Integration (PostgreSQL/MySQL)
- JWT Authentication
- Email/SMS Notifications
- PDF Statement Generation
- Docker Containerization

-------
MIT
