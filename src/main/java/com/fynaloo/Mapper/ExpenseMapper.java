package com.fynaloo.Mapper;

import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.PaidDebtInfoDTO;
import com.fynaloo.Dto.ParticipantInfoDTO;
import com.fynaloo.Model.Entity.Expense;
import com.fynaloo.Model.Entity.ExpenseParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(source = "paidBy.username", target = "paidBy")
    @Mapping(target = "participants", expression = "java(mapUnsettledParticipants(expense.getParticipants()))")
    @Mapping(target = "paidDebts", expression = "java(mapSettledDebts(expense.getParticipants()))")
    ExpenseDetailsDTO toExpenseDetailsDTO(Expense expense);

    // MAPPERY POMOCNICZE:

    @Named("mapUnsettledParticipants")
    default Set<ParticipantInfoDTO> mapUnsettledParticipants(Set<ExpenseParticipant> participants) {
        if (participants == null) return Set.of();
        return participants.stream()
                .filter(p -> !p.isSettled())
                .map(p -> new ParticipantInfoDTO(
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getShareAmount(),
                        false
                ))
                .collect(Collectors.toSet());
    }

    @Named("mapSettledDebts")
    default Set<PaidDebtInfoDTO> mapSettledDebts(Set<ExpenseParticipant> participants) {
        if (participants == null) return Set.of(); // Zabezpieczenie przed null
        return participants.stream()
                .filter(ExpenseParticipant::isSettled)
                .map(p -> new PaidDebtInfoDTO(
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getShareAmount()
                ))
                .collect(Collectors.toSet());
    }
}