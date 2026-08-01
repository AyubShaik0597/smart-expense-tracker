package com.expensetracker.dto;

import java.util.List;


public class TotalsBreakdownResponse {

    private double overallTotal;
    private List<TotalResponse> byCategory;

    public TotalsBreakdownResponse(double overallTotal, List<TotalResponse> byCategory) {
        this.overallTotal = overallTotal;
        this.byCategory = byCategory;
    }

    public double getOverallTotal() {
        return overallTotal;
    }

    public List<TotalResponse> getByCategory() {
        return byCategory;
    }
}
