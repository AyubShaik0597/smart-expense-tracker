package com.expensetracker.service;

import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.dto.TotalResponse;
import com.expensetracker.exception.ExpenseNotFoundException;
import com.expensetracker.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

    private ExpenseService service;

    @BeforeEach
    void setUp() {
        service = new ExpenseService(new com.expensetracker.repository.ExpenseRepository());
    }

    private ExpenseRequest request(String title, double amount, String category, LocalDate date) {
        ExpenseRequest req = new ExpenseRequest();
        req.setTitle(title);
        req.setAmount(amount);
        req.setCategory(category);
        req.setDate(date);
        return req;
    }

    @Test
    void addExpense_assignsIdAndStoresIt() {
        Expense saved = service.addExpense(request("Coffee", 4.50, "Food", LocalDate.of(2026, 1, 5)));

        assertNotNull(saved.getId());
        assertEquals("Coffee", saved.getTitle());
        assertEquals(1, service.getAllExpenses().size());
    }

    @Test
    void getAllExpenses_returnsNewestFirst() {
        service.addExpense(request("Old", 10, "Food", LocalDate.of(2026, 1, 1)));
        service.addExpense(request("New", 20, "Food", LocalDate.of(2026, 1, 10)));

        List<Expense> all = service.getAllExpenses();
        assertEquals("New", all.get(0).getTitle());
        assertEquals("Old", all.get(1).getTitle());
    }

    @Test
    void getExpensesByCategory_isCaseInsensitive() {
        service.addExpense(request("Bus ticket", 2.0, "Transport", LocalDate.of(2026, 1, 3)));
        service.addExpense(request("Snack", 1.5, "food", LocalDate.of(2026, 1, 3)));

        List<Expense> transport = service.getExpensesByCategory("transport");
        assertEquals(1, transport.size());
        assertEquals("Bus ticket", transport.get(0).getTitle());
    }

    @Test
    void deleteExpense_removesIt() {
        Expense saved = service.addExpense(request("Movie", 12, "Entertainment", LocalDate.now()));

        service.deleteExpense(saved.getId());

        assertTrue(service.getAllExpenses().isEmpty());
    }

    @Test
    void deleteExpense_unknownId_throwsNotFound() {
        assertThrows(ExpenseNotFoundException.class, () -> service.deleteExpense(999L));
    }

    @Test
    void getOverallTotal_sumsAllExpenses() {
        service.addExpense(request("A", 10.50, "Food", LocalDate.now()));
        service.addExpense(request("B", 5.25, "Transport", LocalDate.now()));

        assertEquals(15.75, service.getOverallTotal());
    }

    @Test
    void getTotalForCategory_onlySumsThatCategory() {
        service.addExpense(request("A", 10, "Food", LocalDate.now()));
        service.addExpense(request("B", 20, "Food", LocalDate.now()));
        service.addExpense(request("C", 5, "Transport", LocalDate.now()));

        TotalResponse foodTotal = service.getTotalForCategory("Food");

        assertEquals(30.0, foodTotal.getTotal());
        assertEquals(2, foodTotal.getCount());
    }

    @Test
    void getTotalForCategory_withNoExpenses_returnsZero() {
        TotalResponse total = service.getTotalForCategory("Nonexistent");

        assertEquals(0.0, total.getTotal());
        assertEquals(0, total.getCount());
    }
}
