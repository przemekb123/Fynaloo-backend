package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.ExpenseRequest;
import com.fynaloo.Dto.ParticipantInfoDTO;
import com.fynaloo.Mapper.ExpenseMapper;
import com.fynaloo.Model.Entity.Expense;
import com.fynaloo.Model.Entity.ExpenseParticipant;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.ExpenseParticipantRepository;
import com.fynaloo.Repository.ExpenseRepository;
import com.fynaloo.Repository.GroupRepository;
import com.fynaloo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseParticipantRepository expenseParticipantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    private ExpenseRequest inputDTO;
    private User payer;
    private User participantUser;
    private Group group;
    private Expense savedExpense;
    private ExpenseDetailsDTO expectedDTO;

    @BeforeEach
    void setUp() {
        // Arrange common test data
        Long groupId = 1L;
        Long userId1 = 2L;
        String payerUsername = "payer";

        inputDTO = new ExpenseRequest();
        inputDTO.setGroupId(groupId);
        inputDTO.setDescription("Dinner");
        inputDTO.setAmount(BigDecimal.valueOf(100));
        inputDTO.setCurrency("USD");
        inputDTO.setPaidBy(payerUsername);

        ParticipantInfoDTO participant = new ParticipantInfoDTO();
        participant.setUserId(userId1);
        participant.setUsername("participant");
        participant.setShareAmount(BigDecimal.valueOf(50));
        participant.setSettled(false);
        inputDTO.setParticipants(Set.of(participant));

        payer = new User();
        payer.setId(1L);
        payer.setUsername(payerUsername);

        group = new Group();
        group.setId(groupId);

        participantUser = new User();
        participantUser.setId(userId1);
        participantUser.setUsername("participant");

        savedExpense = new Expense();
        savedExpense.setId(10L);

        expectedDTO = new ExpenseDetailsDTO();
        expectedDTO.setId(10L);
        expectedDTO.setDescription(inputDTO.getDescription());
        expectedDTO.setAmount(inputDTO.getAmount());
        expectedDTO.setCurrency(inputDTO.getCurrency());
        expectedDTO.setPaidBy(inputDTO.getPaidBy());
        expectedDTO.setParticipants(inputDTO.getParticipants());
    }

    @Test
    void createExpense_success() {
        // Arrange
        when(userRepository.findByUsername(inputDTO.getPaidBy())).thenReturn(Optional.of(payer));
        when(groupRepository.findById(inputDTO.getGroupId())).thenReturn(Optional.of(group));
        when(userRepository.findById(inputDTO.getParticipants().iterator().next().getUserId()))
                .thenReturn(Optional.of(participantUser));
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        when(expenseParticipantRepository.save(any(ExpenseParticipant.class))).thenReturn(new ExpenseParticipant());
        when(expenseMapper.toExpenseDetailsDTO(any(Expense.class))).thenReturn(expectedDTO);

        // Act
        ExpenseDetailsDTO result = expenseService.createExpense(inputDTO);

        // Assert
        assertEquals(expectedDTO, result);
        verify(userRepository, times(1)).findByUsername(inputDTO.getPaidBy());
        verify(groupRepository, times(1)).findById(inputDTO.getGroupId());
        verify(userRepository, times(1)).findById(inputDTO.getParticipants().iterator().next().getUserId());
        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(expenseParticipantRepository, times(1)).save(any(ExpenseParticipant.class));
        verify(expenseMapper, times(1)).toExpenseDetailsDTO(any(Expense.class));
    }
}