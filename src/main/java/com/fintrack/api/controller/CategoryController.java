package com.fintrack.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fintrack.api.config.SecurityUtils;
import com.fintrack.api.dto.request.CategoryRequestDTO;
import com.fintrack.api.dto.response.CategoryResponseDTO;
import com.fintrack.api.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @Valid @RequestBody CategoryRequestDTO request) {

        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAlll() {

        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(categoryService.findAllByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        categoryService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
