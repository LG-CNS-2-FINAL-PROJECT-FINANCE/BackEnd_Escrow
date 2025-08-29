package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.client.BalanceClient;
import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.request.BalanceRequest;
import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.BalanceResponse;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import com.ddiring.BackEnd_Escrow.enums.TransType;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.repository.RecordRepository;
import jakarta.transaction.Transactional;
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
    private final BalanceClient balanceClient;

    //거래내역 조회 (projectId)
    public List<HistoryResponse> getRecordsByProjectId(String projectId) {
        Escrow escrow = escrowRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));

        List<Record> records = recordRepository.findAllByEscrow(escrow);

        if (records.isEmpty()) {
            throw new ApplicationException(ErrorCode.ESCROW_HISTORY_NOT_FOUND);
        }

        return records.stream()
                .map(HistoryResponse::fromEntity)
                .toList();
    }

    //잔액 조회
    public BalanceResponse getBalance(String account, String projectId) {
        Integer escrowSeq = getEscrowSeq(account, projectId);

        Integer balance = recordRepository.findBalanceByEscrowSeq(escrowSeq);
        if (balance == null) {
            throw new ApplicationException(ErrorCode.ESCROW_BALANCE_NOT_FOUND);
        }

        String responseProjectId = projectId;
        if (responseProjectId == null && account != null) {
            responseProjectId = escrowRepository.findByAccount(account)
                    .map(Escrow::getProjectId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));
        }

        return new BalanceResponse(responseProjectId, balance);
    }

    //escrowSeq 조회 (account 또는 projectId)
    private Integer getEscrowSeq(String account, String projectId) {
        if (account != null) {
            return escrowRepository.findEscrowSeqByAccount(account)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));
        }

        if (projectId != null) {
            return escrowRepository.findEscrowSeqByProjectId(projectId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));
        }

        throw new IllegalArgumentException("account 또는 projectId 중 하나는 반드시 필요합니다.");
    }

    //거래내역 저장
    @Transactional
    public void saveRecord(SaveRecordRequest saveRecordRequest) {
        //account로 Escrow 조회
        Escrow escrow = getEscrow(saveRecordRequest.getAccount());

        //TransType 변환
        TransType transType = TransType.fromCode(saveRecordRequest.getTransType());
        int flow = getFlowByTransType(transType);

        LocalDateTime now = LocalDateTime.now();
        String userSeq = saveRecordRequest.getUserSeq();
        Integer amount = saveRecordRequest.getAmount();

        //금액 유효성 체크
        validatePositiveAmount(amount, flow == 0
                ? ErrorCode.WITHDRAW_AMOUNT_INVALID.defaultMessage()
                : ErrorCode.DEPOSIT_AMOUNT_INVALID.defaultMessage());

        //출금일 경우 잔액 체크
        if (flow == 0) {
            validateWithdraw(saveRecordRequest.getAccount(), amount);
        }

        //Record 생성
        Record record = Record.builder()
                .escrow(escrow) //account로 조회한 Escrow
                .userSeq(userSeq)
                .transSeq(saveRecordRequest.getTransSeq())
                .transType(transType)
                .amount(amount)
                .flow(flow)
                .escrowStatus(EscrowStatus.COMPLETED)
                .initiatedAt(now)
                .completedAt(now)
                .createdId(userSeq)
                .createdAt(now)
                .updatedId(userSeq)
                .updatedAt(now)
                .build();

        recordRepository.save(record); //DB에 insert

        //투자 입금이면 Product 서비스에 잔액 전송
        if (transType == TransType.INVESTMENT && flow == 1) {
            sendBalanceToOtherService(escrow.getProjectId());
        }
    }

    //투자 상품 계좌 조회
    private Escrow getEscrow(String account) {
        return escrowRepository.findByAccount(account)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ESCROW_NOT_FOUND));
    }

    //TransType에 따른 입출금 플로우
    private int getFlowByTransType(TransType transType) {
        if (transType == null) {
            throw new ApplicationException(ErrorCode.INVALID_TRANS_TYPE);
        }

        return switch (transType) {
            case REFUND, TRADEOUT, DISTRIBUTED -> 0; //출금
            case INVESTMENT, TRADEIN -> 1;                 //입금
        };
    }

    //금액 유효성 체크 (공통)
    private void validatePositiveAmount(Integer amount, String message) {
        if (amount == null || amount <= 0) {
            throw new ApplicationException(ErrorCode.INVALID_PARAMETER, message);
        }
    }

    //출금 검증
    private void validateWithdraw(String account, Integer amount) {
        Integer balance = getBalance(account, null).getBalance();

        if (balance == null || balance < amount) {
            throw new ApplicationException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }


    //Product 서비스에 잔액 전달
    public void sendBalanceToOtherService(String projectId) {
        BalanceResponse balanceResponse = getBalance(null, projectId);
        BalanceRequest request = new BalanceRequest(projectId, balanceResponse.getBalance());
        balanceClient.sendBalance(request);
    }
}