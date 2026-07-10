package com.fintrack.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.api.config.SecurityUtils;
import com.fintrack.api.dto.request.TransactionRequestDTO;
import com.fintrack.api.dto.response.BalanceResponseDTO;
import com.fintrack.api.dto.response.TransactionResponseDTO;
import com.fintrack.api.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create Transaction")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(userId, request));
    }

    @GetMapping
    @Operation(summary = "Find All Transactions from User")
    public ResponseEntity<List<TransactionResponseDTO>> findAllTransactions() {
    
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(transactionService.findAllByUser(userId));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Transaction")
    public ResponseEntity<Void> deleteTransaction( @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        transactionService.deleteTransaction(userId, id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/balance")
    @Operation(summary = "Get Balance", description = "Get the balance of transactions for the authenticated user within a specified date range")
    public ResponseEntity<BalanceResponseDTO> getBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(transactionService.getBalance(userId, startDate, endDate));
    }
}
