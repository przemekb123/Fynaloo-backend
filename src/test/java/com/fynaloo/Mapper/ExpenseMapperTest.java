package com.fynaloo.Mapper;

import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.ParticipantInfoDTO;
import com.fynaloo.Dto.PaidDebtInfoDTO;
import com.fynaloo.Model.Entity.Expense;
import com.fynaloo.Model.Entity.ExpenseParticipant;
import com.fynaloo.Model.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseMapperTest {

    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        expenseMapper = new ExpenseMapperImpl();
    }

    @Test
    void should_map_expense_to_expense_details_dto() {
        // given
        User payer = new User();
        payer.setId(1L);
        payer.setUsername("payerUser");

        User participantUser = new User();
        participantUser.setId(2L);
        participantUser.setUsername("participantUser");

        ExpenseParticipant participant = new ExpenseParticipant();
        participant.setUser(participantUser);
        participant.setShareAmount(new BigDecimal("50.00"));
        participant.setSettled(false);

        Expense expense = new Expense();
        expense.setId(100L);
        expense.setDescription("Dinner");
        expense.setAmount(new BigDecimal("100.00"));
        expense.setPaidBy(payer);
        expense.setCurrency("PLN");
        expense.setParticipants(Set.of(participant));

        // when
        ExpenseDetailsDTO dto = expenseMapper.toExpenseDetailsDTO(expense);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPaidBy()).isEqualTo("payerUser");
        assertThat(dto.getParticipants()).hasSize(1);

        ParticipantInfoDTO participantDTO = dto.getParticipants().iterator().next();
        assertThat(participantDTO.getUsername()).isEqualTo("participantUser");
    }
}
