package com.ddiring.BackEnd_Escrow.dto.request;

import com.ddiring.BackEnd_Escrow.enums.TransType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveRecordRequest {
    private String account;
    private String userSeq;
    private Integer transSeq;
    private Integer transType;
    private BigDecimal amount;
}
