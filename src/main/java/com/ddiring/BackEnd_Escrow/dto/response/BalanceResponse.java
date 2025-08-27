package com.ddiring.BackEnd_Escrow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    private String projectId;    //프로젝트 ID
    private Integer balance;  //잔액
}