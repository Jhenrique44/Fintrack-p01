package com.fintrack.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fintrack.api.dto.request.UserRequestDTO;
import com.fintrack.api.dto.response.UserResponseDTO;
import com.fintrack.api.entity.User;
import com.fintrack.api.exception.BusinessException;
import com.fintrack.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        
        if(userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        User user = User.builder()
                        .name(userRequestDTO.getUsername())
                        .email(userRequestDTO.getEmail())
                        .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                        .build();
        User saved = userRepository.save(user);

        return toResponseDTO(saved);
    }

    private UserResponseDTO toResponseDTO(User user) { 
        return UserResponseDTO.builder()
                          .id(user.getId())
                          .name(user.getName())
                          .email(user.getEmail())
                          .createdAt(user.getCreatedAt())
                          .build();
    }
    
}
