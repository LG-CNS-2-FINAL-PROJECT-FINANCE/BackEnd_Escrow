package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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

        return records.stream().map(HistoryResponse::fromEntity).toList();
    }

    //잔액 조회
    public BigDecimal getBalanceByEscrowSeq(Integer escrowSeq) {
        return recordRepository.findBalanceByEscrowSeq(escrowSeq);
    }

    //거래내역 저장
    public void saveRecord(SaveRecordRequest saveRecordRequest) {
        Escrow escrow = getEscrow(saveRecordRequest.getEscrowSeq());

        TransType transType = TransType.fromCode(saveRecordRequest.getTransType());
        int flow = getFlowByTransType(transType);

        LocalDateTime now = LocalDateTime.now();
        String userSeq = saveRecordRequest.getUserSeq().toString();

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
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트의 계좌가 존재하지 않습니다."));
    }

    //TransType에 따른 입출금 구분
    private int getFlowByTransType(TransType transType) {
        if (transType == null) {
            throw new IllegalArgumentException("transType은 null일 수 없습니다.");
        }

        return switch (transType) {
            case INVESTMENT -> 1;   // 투자: 입금
            case TRADE -> 0;        // 거래: 출금
            case DISTRIBUTED -> 0;  // 분배: 출금
        };
    }

}
