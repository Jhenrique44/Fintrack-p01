package com.fintrack.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fintrack.api.dto.request.TransactionRequestDTO;
import com.fintrack.api.dto.response.BalanceResponseDTO;
import com.fintrack.api.dto.response.TransactionResponseDTO;
import com.fintrack.api.entity.Category;
import com.fintrack.api.entity.Transaction;
import com.fintrack.api.entity.TransactionType;
import com.fintrack.api.entity.User;
import com.fintrack.api.exception.BusinessException;
import com.fintrack.api.repository.CategoryRepository;
import com.fintrack.api.repository.TransactionRepository;
import com.fintrack.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionResponseDTO createTransaction(Long userId, TransactionRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new BusinessException("Category does not belong to the user");
        }

        if (!category.getType().equals(request.getType())) {
            throw new BusinessException("Category type does not match transaction type");
        }

        Transaction transaction = Transaction.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .date(request.getDate())
                .type(request.getType())
                .category(category)
                .user(user)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return toResponseDTO(savedTransaction);
    }

    public List<TransactionResponseDTO> findAllByUser(Long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new BusinessException("Transaction does not belong to the user");
        }
        transactionRepository.delete(transaction);
    }

    public BalanceResponseDTO getBalance(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal income = transactionRepository.sumByUserIdAndTypeAndDateBetween(userId, TransactionType.INCOME,
                startDate, endDate);
        BigDecimal expense = transactionRepository.sumByUserIdAndTypeAndDateBetween(userId, TransactionType.EXPENSE,
                startDate, endDate);

        return BalanceResponseDTO.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    private TransactionResponseDTO toResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .type(transaction.getType())
                .categoryName(transaction.getCategory().getName())
                .build();
    }
}
