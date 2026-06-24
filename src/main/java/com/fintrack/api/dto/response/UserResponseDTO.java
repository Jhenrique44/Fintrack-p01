package com.fintrack.api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
