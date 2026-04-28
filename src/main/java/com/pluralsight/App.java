package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    private LocalDate Date;
    private LocalTime time;
    private String description,vendor;
    private double amount;

    public App(double amount, String vendor, LocalTime time, String description, LocalDate date) {
        this.amount = amount;
        this.vendor = vendor;
        this.time = time;
        this.description = description;
        Date = date;

    }

    public LocalDate getDate() {
        return Date;
    }

    public double getAmount() {
        return amount;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getTime() {
        return time;
    }
}
