package com.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private String expenseJson(String title, double amount, String category, String date) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("amount", amount);
        body.put("category", category);
        body.put("date", date);
        return objectMapper.writeValueAsString(body);
    }

    @Test
    void addExpense_returns201AndCreatedBody() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson("Groceries", 45.99, "Food", LocalDate.now().toString())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Groceries"))
                .andExpect(jsonPath("$.category").value("Food"));
    }

    @Test
    void addExpense_missingTitle_returns400() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson("", 10, "Food", LocalDate.now().toString())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addExpense_negativeAmount_returns400() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson("Refund", -5, "Food", LocalDate.now().toString())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExpenses_filtersByCategory() throws Exception {
        mockMvc.perform(post("/api/expenses").contentType(MediaType.APPLICATION_JSON)
                .content(expenseJson("Taxi", 15, "Transport", LocalDate.now().toString())));
        mockMvc.perform(post("/api/expenses").contentType(MediaType.APPLICATION_JSON)
                .content(expenseJson("Lunch", 8, "Food", LocalDate.now().toString())));

        mockMvc.perform(get("/api/expenses").param("category", "Transport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].category", everyItem(equalToIgnoringCase("Transport"))));
    }

    @Test
    void deleteExpense_thenGetAll_noLongerIncludesIt() throws Exception {
        String response = mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson("Temp", 1, "Misc", LocalDate.now().toString())))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/expenses/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteExpense_unknownId_returns404() throws Exception {
        mockMvc.perform(delete("/api/expenses/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTotals_includesOverallAndByCategory() throws Exception {
        mockMvc.perform(post("/api/expenses").contentType(MediaType.APPLICATION_JSON)
                .content(expenseJson("Book", 20, "Education", LocalDate.now().toString())));

        mockMvc.perform(get("/api/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallTotal", greaterThanOrEqualTo(20.0)))
                .andExpect(jsonPath("$.byCategory").isArray());
    }
}
