package com.ddiring.BackEnd_Escrow.dto.response;

import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private Integer recordSeq;          //기록번호
    private Integer userSeq;            //사용자번호
    private Integer transSeq;           //주문번호
    private TransType transType;        //주문종류 enum
    private BigDecimal amount;          //금액
    private Integer flow;               //입출금 out = 0, in = 1
    private EscrowStatus escrowStatus;  //에스크로 상태 enum
    private LocalDateTime initiatedAt;  //주문발생시간
    private LocalDateTime completedAt;  //주문완료시간

    public static HistoryResponse fromEntity(Record record) {
        return HistoryResponse.builder()
                .recordSeq(record.getRecordSeq())
                .userSeq(record.getUserSeq())
                .transSeq(record.getTransSeq())
                .transType(record.getTransType())
                .amount(record.getAmount())
                .flow(record.getFlow())
                .escrowStatus(record.getEscrowStatus())
                .initiatedAt(record.getInitiatedAt())
                .completedAt(record.getCompletedAt())
                .build();
    }
}