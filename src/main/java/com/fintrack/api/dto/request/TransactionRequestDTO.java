package com.fintrack.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fintrack.api.entity.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TransactionRequestDTO {
    

    @NotBlank(message = "Transaction name cannot be empty")
    private String description;

    @NotNull(message = "Transaction amount cannot be null")
    @DecimalMin(value = "0.01", inclusive = false, message = "Transaction amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transaction date cannot be null")
    private LocalDate date;

    @NotNull(message = "Transaction type cannot be null")
    private TransactionType type;

    @NotNull(message = "Category cannot be null")
    private Long categoryId;
}
