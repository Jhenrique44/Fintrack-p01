package com.fintrack.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fintrack.api.dto.request.CategoryRequestDTO;
import com.fintrack.api.dto.response.CategoryResponseDTO;
import com.fintrack.api.entity.Category;
import com.fintrack.api.entity.User;
import com.fintrack.api.exception.BusinessException;
import com.fintrack.api.repository.CategoryRepository;
import com.fintrack.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponseDTO createCategory(Long userId, CategoryRequestDTO request)  {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));
            
        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(user)
                .build();

        Category saved = categoryRepository.save(category);
        
        return toResponseDTO(saved);
    }

    public List<CategoryResponseDTO> findAllByUser(Long userId) { 
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void delete(Long userId, Long categoryId) { 
        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new BusinessException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new BusinessException("Category does not belong to the user");
        }
        categoryRepository.delete(category);
    }
    
    private CategoryResponseDTO toResponseDTO(Category category) { 
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }
}
