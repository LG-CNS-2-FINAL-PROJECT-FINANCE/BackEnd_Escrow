package com.ddiring.BackEnd_Escrow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    private Integer escrowSeq;  //에스크로 번호
    private BigDecimal balance;  //잔액
}