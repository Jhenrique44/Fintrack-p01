package com.fintrack.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.api.dto.request.TransactionRequestDTO;
import com.fintrack.api.dto.response.TransactionResponseDTO;
import com.fintrack.api.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestParam Long userId,
            @Valid @RequestBody TransactionRequestDTO request) {
        TransactionResponseDTO responseTransaction = transactionService.createTransaction(userId, request);
        return ResponseEntity.ok(responseTransaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAllTransactions(@RequestParam Long userId) {
        return ResponseEntity.ok(transactionService.findAllByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@RequestParam Long userId, @PathVariable Long id) {
        transactionService.deleteTransaction(userId, id);

        return ResponseEntity.noContent().build();
    }
}
