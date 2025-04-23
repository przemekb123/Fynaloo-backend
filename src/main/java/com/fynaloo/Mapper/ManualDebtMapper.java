package com.fynaloo.Mapper;

import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Model.Entity.ManualDebt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManualDebtMapper {


    ManualDebtDetailsDTO toManualDebtDetailsDTO(ManualDebt manualDebt);
}
