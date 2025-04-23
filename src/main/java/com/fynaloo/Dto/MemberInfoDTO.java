package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDTO {
    private Long userId;         // ID użytkownika
    private String username;
    private String email;
    private String role;
}
