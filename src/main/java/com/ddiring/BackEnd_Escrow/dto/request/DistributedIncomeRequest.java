package com.ddiring.BackEnd_Escrow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DistributedIncomeRequest {
    private String account;
    private String userSeq;
    private Integer distributionAmount;
}