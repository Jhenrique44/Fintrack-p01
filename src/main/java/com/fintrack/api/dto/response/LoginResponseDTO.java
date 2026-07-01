package com.fintrack.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    

    private String token;
    private Long userId;
    private String name;
    private String email;
}
