package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.dto.TotalResponse;
import com.expensetracker.dto.TotalsBreakdownResponse;
import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expenses", description = "Manage personal expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Add a new expense")
    @PostMapping
    public ResponseEntity<Expense> addExpense(@Valid @RequestBody ExpenseRequest request) {
        Expense created = expenseService.addExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "View all expenses, optionally filtered by category")
    @GetMapping
    public List<Expense> getExpenses(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return expenseService.getExpensesByCategory(category);
        }
        return expenseService.getAllExpenses();
    }

    @Operation(summary = "Delete an expense by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
       // return ResponseEntity.noContent().build();
        return ResponseEntity.ok("Expense deleted successfully.");
    }

    @Operation(summary = "Overall total, plus a breakdown by category")
    @GetMapping("/total")
    public TotalsBreakdownResponse getTotals() {
        return expenseService.getTotalsBreakdown();
    }

    @Operation(summary = "Total for a single category")
    @GetMapping("/total/{category}")
    public TotalResponse getTotalForCategory(@PathVariable String category) {
        return expenseService.getTotalForCategory(category);
    }
}
