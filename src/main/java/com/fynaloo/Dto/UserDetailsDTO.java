package com.fynaloo.Dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<UserFinancialSummaryDTO> debts;
    private Set<GroupDetailsDTO> groups;
}
