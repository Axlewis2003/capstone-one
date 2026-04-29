package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    private LocalDate date;
    private LocalTime time;
    private String description,vendor;
    private double amount;

    public App(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;

    }

    public LocalDate getDate() {
        return date;
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

    public String format() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;

    }

}

