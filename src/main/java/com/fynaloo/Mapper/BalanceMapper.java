package com.fynaloo.Mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.fynaloo.Dto.BalanceSummaryDTO;
import com.fynaloo.Model.Entity.Balance;
import com.fynaloo.Model.Entity.User;

import org.mapstruct.Context;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    @Mapping(target = "userId", expression = "java(resolveOtherUser(balance, currentUser))")
    @Mapping(target = "username", expression = "java(resolveOtherUsername(balance, currentUser))")
    @Mapping(target = "amountPln", expression = "java(resolveSignedBalance(balance, currentUser))")
    BalanceSummaryDTO toBalanceSummaryDTO(Balance balance, @Context User currentUser);

    @Named("resolveOtherUser")
    default Long resolveOtherUser(Balance balance, User currentUser) {
        return balance.getUser1().getId().equals(currentUser.getId())
                ? balance.getCounterparty().getId()
                : balance.getUser1().getId();
    }

    @Named("resolveOtherUsername")
    default String resolveOtherUsername(Balance balance, User currentUser) {
        return balance.getUser1().getId().equals(currentUser.getId())
                ? balance.getCounterparty().getUsername()
                : balance.getUser1().getUsername();
    }

    @Named("resolveSignedBalance")
    default BigDecimal resolveSignedBalance(Balance balance, User currentUser) {
        return balance.getUser1().getId().equals(currentUser.getId())
                ? balance.getBalance()
                : balance.getBalance().negate();
    }
}



