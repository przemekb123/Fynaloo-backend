package com.fynaloo.Mapper;

import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Model.Entity.ManualDebt;
import com.fynaloo.Model.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ManualDebtMapperTest {

    private ManualDebtMapper manualDebtMapper;

    @BeforeEach
    void setUp() {
        manualDebtMapper = Mappers.getMapper(ManualDebtMapper.class);
    }

    @Test
    void should_map_manual_debt_to_manual_debt_details_dto() {
        // given
        User debtor = new User();
        debtor.setId(1L);
        debtor.setUsername("debtorUser");

        User creditor = new User();
        creditor.setId(2L);
        creditor.setUsername("creditorUser");

        ManualDebt manualDebt = new ManualDebt();
        manualDebt.setId(100L);
        manualDebt.setDebtor(debtor);
        manualDebt.setCreditor(creditor);
        manualDebt.setAmount(new BigDecimal("250.00"));
        manualDebt.setSettled(false);
        manualDebt.setCreatedAt(LocalDateTime.now());
        manualDebt.setDescription("Loan for dinner");

        // when
        ManualDebtDetailsDTO dto = manualDebtMapper.toManualDebtDetailsDTO(manualDebt);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getDebtor()).isEqualTo(debtor);
        assertThat(dto.getCreditor()).isEqualTo(creditor);
        assertThat(dto.getAmount()).isEqualTo(new BigDecimal("250.00"));
        assertThat(dto.getSettled()).isFalse();
        assertThat(dto.getDescription()).isEqualTo("Loan for dinner");
        assertThat(dto.getCreatedAt()).isNotNull();
    }
}
