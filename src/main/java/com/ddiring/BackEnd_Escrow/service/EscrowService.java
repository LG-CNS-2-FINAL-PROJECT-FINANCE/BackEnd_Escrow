package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EscrowService {
    private final EscrowRepository escrowRepository;

    //에스크로 계좌 개설
    public CreateAccountResponse createAccount(Integer projectSeq, String userSeq) {
        String accountNumber = generateUniqueAccountNumber();

        Escrow escrow = Escrow.builder()
                .projectSeq(projectSeq)
                .account(accountNumber)
                .createdId(userSeq)
                .createdAt(LocalDateTime.now())
                .updatedId(userSeq)
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            escrowRepository.save(escrow);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorCode.DUPLICATE_ACCOUNT);
        }

        return CreateAccountResponse.fromEntity(escrow);
    }

    //중복체크 후 계좌번호 생성
    private String generateUniqueAccountNumber() {
        String accountNumber;
        int maxAttempts = 5;
        int attempt = 0;

        do {
            if (attempt >= maxAttempts) {
                throw new ApplicationException(ErrorCode.SERVER_ERROR, "계좌번호 생성 실패: 중복이 너무 많이 발생했습니다.");
            }
            accountNumber = generateAccountNumber();
            attempt++;
        } while (escrowRepository.existsByAccount(accountNumber));

        return accountNumber;
    }

    //계좌번호 생성
    private String generateAccountNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); //ex) 20250730

        int randomPart = new Random().nextInt(900000) + 100000; //6자리 난수
        return datePart + randomPart; //ex) 20250730123456 (14자리)
    }
}