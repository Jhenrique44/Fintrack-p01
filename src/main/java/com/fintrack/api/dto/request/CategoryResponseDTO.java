package com.fintrack.api.dto.request;

import com.fintrack.api.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private TransactionType type;
}
