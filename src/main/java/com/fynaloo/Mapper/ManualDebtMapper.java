package com.fynaloo.Mapper;

import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Model.Entity.ManualDebt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManualDebtMapper {

    @Mapping(source = "debtor.username", target = "debtor")
    @Mapping(source = "creditor.username", target = "creditor")
    @Mapping(source = "creditor.firstName", target = "creditorFirstName")
    @Mapping(source = "creditor.lastName", target = "creditorLastName")
    ManualDebtDetailsDTO toManualDebtDetailsDTO(ManualDebt manualDebt);
}
