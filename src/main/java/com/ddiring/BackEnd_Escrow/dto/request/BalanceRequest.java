package com.ddiring.BackEnd_Escrow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequest {
    private String projectId;
    private Integer balance;
}
