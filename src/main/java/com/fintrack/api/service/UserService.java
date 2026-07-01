package com.fintrack.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fintrack.api.dto.request.LoginRequestDTO;
import com.fintrack.api.dto.request.UserRequestDTO;
import com.fintrack.api.dto.response.LoginResponseDTO;
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
    private final JwtService jwtService;

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


    public LoginResponseDTO login(LoginRequestDTO request) { 
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return LoginResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
