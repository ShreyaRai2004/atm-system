package com.atm;

import com.atm.service.AtmService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AtmConsole implements CommandLineRunner {

    private final AtmService atmService;

    public AtmConsole(AtmService atmService) {
        this.atmService = atmService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        String userId = "";
        String accountNumber = "";

        System.out.println("\n========================================");
        System.out.println("            ATM SYSTEM                 ");
        System.out.println("========================================\n");

        while (true) {
            System.out.println("========== MAIN MENU ==========");
            System.out.println("1. Login");
            System.out.println("2. Check Balance");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Change PIN");
            System.out.println("7. Balance History");
            System.out.println("8. Transaction History");
            System.out.println("9. Loan Eligibility");
            System.out.println("10. Bank Statistics");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.print("Enter PIN: ");
                    String pin = scanner.nextLine();
                    System.out.println(atmService.login(userId, pin));
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLine();
                    System.out.println(atmService.checkBalance(accountNumber));
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter Amount: ");
                    double depositAmt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println(atmService.deposit(accountNumber, depositAmt));
                    break;

                case 4:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter Amount: ");
                    double withdrawAmt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.println(atmService.withdraw(accountNumber, withdrawAmt, userId));
                    break;

                case 5:
                    System.out.print("From Account: ");
                    String fromAcc = scanner.nextLine();
                    System.out.print("To Account: ");
                    String toAcc = scanner.nextLine();
                    System.out.print("Amount: ");
                    double transferAmt = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.println(atmService.transfer(fromAcc, toAcc, transferAmt, userId));
                    break;

                case 6:
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.print("Old PIN: ");
                    String oldPin = scanner.nextLine();
                    System.out.print("New PIN: ");
                    String newPin = scanner.nextLine();
                    System.out.println(atmService.changePin(userId, oldPin, newPin));
                    break;

                case 7:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLine();
                    atmService.showBalanceHistory(accountNumber);
                    break;

                case 8:
                    System.out.print("Enter Account Number: ");
                    accountNumber = scanner.nextLine();
                    System.out.println(atmService.getTransactionHistory(accountNumber));
                    break;

                case 9:
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.println(atmService.checkLoanEligibility(userId));
                    break;

                case 10:
                    atmService.showBankStats();
                    break;

                case 0:
                    System.out.println("\nThank you for using ATM System!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }
}