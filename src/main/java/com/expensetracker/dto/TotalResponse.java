package com.expensetracker.dto;

public class TotalResponse {

    private String category; // null when this is the overall total
    private double total;
    private int count;

    public TotalResponse(String category, double total, int count) {
        this.category = category;
        this.total = total;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }
}
