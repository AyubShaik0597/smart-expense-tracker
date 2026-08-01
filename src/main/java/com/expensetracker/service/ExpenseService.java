package com.expensetracker.service;

import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.dto.TotalResponse;
import com.expensetracker.dto.TotalsBreakdownResponse;
import com.expensetracker.exception.ExpenseNotFoundException;
import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense addExpense(ExpenseRequest request) {
        Expense expense = new Expense(
                null,
                request.getTitle(),
                request.getAmount(),
                normalizeCategory(request.getCategory()),
                request.getDate()
        );
        return repository.save(expense);
    }

    /** All expenses, most recent date first. */
    public List<Expense> getAllExpenses() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /** Case-insensitive category filter, e.g. "food" matches "Food". */
    public List<Expense> getExpensesByCategory(String category) {
        String normalized = normalizeCategory(category);
        return repository.findAll().stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(normalized))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public void deleteExpense(Long id) {
        boolean removed = repository.deleteById(id);
        if (!removed) {
            throw new ExpenseNotFoundException(id);
        }
    }

    public double getOverallTotal() {
        return round2(repository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum());
    }

    public TotalResponse getTotalForCategory(String category) {
        String normalized = normalizeCategory(category);
        List<Expense> matches = getExpensesByCategory(normalized);
        double total = round2(matches.stream().mapToDouble(Expense::getAmount).sum());
        return new TotalResponse(normalized, total, matches.size());
    }

    /** Overall total plus a breakdown per category, for a single summary call. */
    public TotalsBreakdownResponse getTotalsBreakdown() {
        Map<String, List<Expense>> grouped = repository.findAll().stream()
                .collect(Collectors.groupingBy(Expense::getCategory));

        List<TotalResponse> byCategory = grouped.entrySet().stream()
                .map(entry -> {
                    double total = round2(entry.getValue().stream().mapToDouble(Expense::getAmount).sum());
                    return new TotalResponse(entry.getKey(), total, entry.getValue().size());
                })
                .sorted(Comparator.comparing(TotalResponse::getTotal).reversed())
                .collect(Collectors.toList());

        return new TotalsBreakdownResponse(getOverallTotal(), byCategory);
    }

    private String normalizeCategory(String category) {
        return category == null ? null : category.trim();
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
