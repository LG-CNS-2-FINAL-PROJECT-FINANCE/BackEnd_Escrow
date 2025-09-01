package com.ddiring.BackEnd_Escrow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVerifyRequest {
    private String account;      // 계좌번호로 조회
    private String userSeq;      // 거래 사용자
    private Integer transType;   // 숫자 코드 (service에서 enum 변환)
    private Integer amount;      // 확인할 금액
}