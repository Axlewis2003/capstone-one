package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AcctLedgerApp {

    // The CSV file where all transactions are stored
    static String FILE_NAME = "transactions.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Alejandro's Ledger");

        // Keep looping until user chooses to exit
        while (true) {
            System.out.println("\n--- HOME SCREEN ---");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Select from the following options: ");
            String userOption = scanner.nextLine().toUpperCase();

            switch (userOption) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    makePayment(scanner);
                    break;
                case "L":
                    showLedger(scanner);
                    break;
                case "X":
                    System.out.println("Bye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  ADD DEPOSIT
    // ─────────────────────────────────────────────
    public static void addDeposit(Scanner scanner) {
        try {
            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Deposits are positive amounts
            saveTransaction(description, vendor, Math.abs(amount));
            System.out.println("Deposit saved successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }

    // ─────────────────────────────────────────────
    //  MAKE PAYMENT (DEBIT)
    // ─────────────────────────────────────────────
    public static void makePayment(Scanner scanner) {
        try {
            System.out.print("Description: ");
            String description = scanner.nextLine();

            System.out.print("Vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Payments are stored as negative amounts
            saveTransaction(description, vendor, -Math.abs(amount));
            System.out.println("Payment saved successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }

    // ─────────────────────────────────────────────
    //  SAVE TRANSACTION TO CSV
    // ─────────────────────────────────────────────
    public static void saveTransaction(String description, String vendor, double amount) {
        // Get today's date and current time automatically
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try (FileWriter fw = new FileWriter(FILE_NAME, true)) { // true = append mode
            fw.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  LOAD ALL TRANSACTIONS FROM CSV
    // ─────────────────────────────────────────────
    public static ArrayList<String[]> loadTransactions() {
        ArrayList<String[]> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                if (!line.trim().isEmpty()) {
                    transactions.add(line.split("\\|"));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }

        // Reverse so newest entries appear first
        ArrayList<String[]> reversed = new ArrayList<>();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            reversed.add(transactions.get(i));
        }
        return reversed;
    }

    // ─────────────────────────────────────────────
    //  DISPLAY TRANSACTIONS
    // ─────────────────────────────────────────────
    public static void displayTransactions(ArrayList<String[]> list) {
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.printf("%-12s %-10s %-30s %-20s %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(85));
        for (String[] t : list) {
            System.out.printf("%-12s %-10s %-30s %-20s %10s%n",
                    t[0], t[1], t[2], t[3], t[4]);
        }
    }

    // ─────────────────────────────────────────────
    //  LEDGER SCREEN
    // ─────────────────────────────────────────────
    public static void showLedger(Scanner scanner) {
        while (true) {
            System.out.println("\n--- LEDGER ---");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    displayTransactions(loadTransactions());
                    break;
                case "D":
                    // Show only positive amounts (deposits)
                    ArrayList<String[]> deposits = new ArrayList<>();
                    for (String[] t : loadTransactions()) {
                        if (Double.parseDouble(t[4]) > 0) deposits.add(t);
                    }
                    displayTransactions(deposits);
                    break;
                case "P":
                    // Show only negative amounts (payments)
                    ArrayList<String[]> payments = new ArrayList<>();
                    for (String[] t : loadTransactions()) {
                        if (Double.parseDouble(t[4]) < 0) payments.add(t);
                    }
                    displayTransactions(payments);
                    break;
                case "R":
                    showReports(scanner);
                    break;
                case "H":
                    return; // go back to home
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  REPORTS SCREEN
    // ─────────────────────────────────────────────
    public static void showReports(Scanner scanner) {
        while (true) {
            System.out.println("\n--- REPORTS ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose a report: ");
            String choice = scanner.nextLine();

            LocalDate today = LocalDate.now();
            ArrayList<String[]> all = loadTransactions();
            ArrayList<String[]> result = new ArrayList<>();

            switch (choice) {
                case "1": // Month To Date
                    for (String[] t : all) {
                        LocalDate d = LocalDate.parse(t[0]);
                        if (d.getYear() == today.getYear() && d.getMonthValue() == today.getMonthValue()) {
                            result.add(t);
                        }
                    }
                    displayTransactions(result);
                    break;

                case "2": // Previous Month
                    LocalDate prevMonth = today.minusMonths(1);
                    for (String[] t : all) {
                        LocalDate d = LocalDate.parse(t[0]);
                        if (d.getYear() == prevMonth.getYear() && d.getMonthValue() == prevMonth.getMonthValue()) {
                            result.add(t);
                        }
                    }
                    displayTransactions(result);
                    break;

                case "3": // Year To Date
                    for (String[] t : all) {
                        LocalDate d = LocalDate.parse(t[0]);
                        if (d.getYear() == today.getYear()) result.add(t);
                    }
                    displayTransactions(result);
                    break;

                case "4": // Previous Year
                    int lastYear = today.getYear() - 1;
                    for (String[] t : all) {
                        LocalDate d = LocalDate.parse(t[0]);
                        if (d.getYear() == lastYear) result.add(t);
                    }
                    displayTransactions(result);
                    break;

                case "5": // Search by Vendor
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().toLowerCase();
                    for (String[] t : all) {
                        if (t[3].toLowerCase().contains(vendor)) result.add(t);
                    }
                    displayTransactions(result);
                    break;

                case "0": // Back to Ledger
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
