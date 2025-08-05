package com.ddiring.BackEnd_Escrow.dto.response;

import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceResponse {
    private Integer escrowSeq;  //에스크로 번호
    private BigDecimal balance;  //잔액
}