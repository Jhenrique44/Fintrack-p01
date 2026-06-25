package com.fintrack.api.dto.request;

import com.fintrack.api.entity.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryRequestDTO { 

    @NotBlank(message = "Category name cannot be empty")
    private String name;

    @NotNull(message = "Category type cannot be null")
    private TransactionType type;
}
