package com.fintrack.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fintrack.api.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id; 
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;  
    private String categoryName;


}
