package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.AdjustPriceDifferenceRequest;
import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Dto.ManualDebtRequest;
import com.fynaloo.Dto.RecalculateDebtRequest;
import com.fynaloo.Mapper.ManualDebtMapper;
import com.fynaloo.Model.Entity.ManualDebt;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.ManualDebtRepository;
import com.fynaloo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManualDebtServiceImplTest {

    @Mock
    private ManualDebtRepository manualDebtRepository;

    @Mock
    private ManualDebtMapper manualDebtMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ManualDebtServiceImpl manualDebtService;

    private User user1;
    private User user2;
    private ManualDebt debt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        debt = new ManualDebt();
        debt.setId(1L);
        debt.setDebtor(user1);
        debt.setCreditor(user2);
        debt.setAmount(BigDecimal.valueOf(100));
        debt.setCreatedAt(LocalDateTime.now());
        debt.setSettled(false);
    }


    @Test
    void should_add_manual_debt() {
        ManualDebtRequest request = new ManualDebtRequest();
        request.setDebtor(user1.getUsername());
        request.setCreditor(user2.getUsername());
        request.setAmount(BigDecimal.valueOf(100));
        request.setDescription("Lunch");

        ManualDebtDetailsDTO detailsDTO = new ManualDebtDetailsDTO();
        detailsDTO.setId(1L);
        detailsDTO.setDebtor(user1.getUsername());
        detailsDTO.setCreditor(user2.getUsername());
        detailsDTO.setAmount(BigDecimal.valueOf(100));
        detailsDTO.setSettled(false);
        detailsDTO.setDescription("Lunch");
        detailsDTO.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        when(manualDebtRepository.save(any(ManualDebt.class))).thenReturn(new ManualDebt());
        when(manualDebtMapper.toManualDebtDetailsDTO(any(ManualDebt.class))).thenReturn(detailsDTO);

        ManualDebtDetailsDTO result = manualDebtService.addManualDebt(request);

        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(result.getDebtor()).isEqualTo(user1.getUsername());
        assertThat(result.getCreditor()).isEqualTo(user2.getUsername());
        verify(manualDebtRepository).save(any(ManualDebt.class));
    }

    @Test
    void should_get_debts_for_user() {
        when(manualDebtRepository.findByDebtorId(1L)).thenReturn(List.of(debt));
        when(manualDebtMapper.toManualDebtDetailsDTO(any(ManualDebt.class))).thenReturn(new ManualDebtDetailsDTO());

        List<ManualDebtDetailsDTO> debts = manualDebtService.getDebtsForUser(1L);

        assertThat(debts).hasSize(1);
        verify(manualDebtRepository).findByDebtorId(1L);
    }

    @Test
    void should_settle_manual_debt() {
        when(manualDebtRepository.findById(1L)).thenReturn(Optional.of(debt));

        manualDebtService.settleManualDebt(1L);

        assertThat(debt.getSettled()).isTrue();
        verify(manualDebtRepository).save(debt);
    }

    @Test
    void should_recalculate_debt_correctly() {
        ManualDebt debtFromDebtor = new ManualDebt();
        debtFromDebtor.setDebtor(user1);
        debtFromDebtor.setCreditor(user2);
        debtFromDebtor.setAmount(BigDecimal.valueOf(100));
        debtFromDebtor.setSettled(false);

        ManualDebt debtFromCreditor = new ManualDebt();
        debtFromCreditor.setDebtor(user2);
        debtFromCreditor.setCreditor(user1);
        debtFromCreditor.setAmount(BigDecimal.valueOf(30));
        debtFromCreditor.setSettled(false);

        when(manualDebtRepository.findByDebtorIdAndCreditorId(1L, 2L)).thenReturn(List.of(debtFromDebtor));
        when(manualDebtRepository.findByDebtorIdAndCreditorId(2L, 1L)).thenReturn(List.of(debtFromCreditor));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        RecalculateDebtRequest request = new RecalculateDebtRequest(1L, 2L, BigDecimal.ZERO);


        verify(manualDebtRepository, atLeastOnce()).save(any(ManualDebt.class));
    }

    @Test
    void should_adjust_price_difference() {
        AdjustPriceDifferenceRequest request = new AdjustPriceDifferenceRequest(
                1L,
                BigDecimal.valueOf(90),
                Set.of(2L)
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        manualDebtService.adjustPriceDifference(request);

        verify(manualDebtRepository, times(1)).save(any(ManualDebt.class));
    }
}