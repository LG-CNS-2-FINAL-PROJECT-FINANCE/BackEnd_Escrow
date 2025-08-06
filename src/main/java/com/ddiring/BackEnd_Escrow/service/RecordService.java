package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final EscrowRepository escrowRepository;
    private final RecordRepository recordRepository;

    //거래내역 조회
    public List<HistoryResponse> getRecordsByEscrowSeq(Integer escrowSeq) {
        List<Record> records = recordRepository.findAllByEscrowSeq(escrowSeq);
        if (records == null || records.isEmpty()) {
            throw new ApplicationException(ErrorCode.ESCROW_HISTORY_NOT_FOUND);
        }
        return records.stream().map(HistoryResponse::fromEntity).toList();
    }

    //잔액 조회
    public BigDecimal getBalanceByEscrowSeq(Integer escrowSeq) {
        boolean exists = escrowRepository.existsByEscrowSeq(escrowSeq);
        if (!exists) {
            throw new ApplicationException(ErrorCode.ESCROW_NOT_FOUND);
        }

        BigDecimal balance = recordRepository.findBalanceByEscrowSeq(escrowSeq);
        if (balance == null) {
            throw new ApplicationException(ErrorCode.ESCROW_BALANCE_NOT_FOUND);
        }

        return balance;
    }

    //거래내역 저장
    public void saveRecord(SaveRecordRequest saveRecordRequest) {
        Escrow escrow = getEscrow(saveRecordRequest.getEscrowSeq());

        TransType transType = TransType.fromCode(saveRecordRequest.getTransType());
        int flow = getFlowByTransType(transType);

        LocalDateTime now = LocalDateTime.now();
        String userSeq = saveRecordRequest.getUserSeq().toString();

        BigDecimal amount = saveRecordRequest.getAmount();
        if (flow == 0) {    //출금
            validateWithdraw(saveRecordRequest.getEscrowSeq(), amount);
        } else {            //입금
            validateDeposit(amount);
        }

        Record record = Record.builder()
                .escrowSeq(saveRecordRequest.getEscrowSeq())
                .userSeq(saveRecordRequest.getUserSeq())
                .transSeq(saveRecordRequest.getTransSeq())
                .transType(transType)
                .amount(saveRecordRequest.getAmount())
                .flow(flow)
                .escrowStatus(EscrowStatus.COMPLETED)
                .initiatedAt(now)
                .completedAt(now)
                .createdId(userSeq)
                .createdAt(now)
                .updatedId(userSeq)
                .updatedAt(now)
                .build();

        recordRepository.save(record);
    }

    //투자 상품 계좌 체크
    private Escrow getEscrow(int escrowSeq) {
        return escrowRepository.findByEscrowSeq(escrowSeq)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));
    }

    //TransType에 따른 입출금 구분
    private int getFlowByTransType(TransType transType) {
        if (transType == null) {
            throw new ApplicationException(ErrorCode.INVALID_TRANS_TYPE);
        }

        return switch (transType) {
            case INVESTMENT -> 1;   // 투자: 입금
            case TRADE -> 0;        // 거래: 출금
            case DISTRIBUTED -> 0;  // 분배: 출금
        };
    }

    //입금 전 잔액 조회
    private void validateDeposit(BigDecimal amount) {
        validatePositiveAmount(amount, ErrorCode.DEPOSIT_AMOUNT_INVALID.defaultMessage());
    }

    //출금 전 잔액 조회
    private void validateWithdraw(Integer escrowSeq, BigDecimal amount) {
        validatePositiveAmount(amount, ErrorCode.WITHDRAW_AMOUNT_INVALID.defaultMessage());

        BigDecimal balance = getBalanceByEscrowSeq(escrowSeq);
        if (balance.compareTo(amount) < 0) {
            throw new ApplicationException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }

    //입출금 금액 체크
    private void validatePositiveAmount(BigDecimal amount, String message) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException(ErrorCode.INVALID_PARAMETER, message);
        }
    }


}
