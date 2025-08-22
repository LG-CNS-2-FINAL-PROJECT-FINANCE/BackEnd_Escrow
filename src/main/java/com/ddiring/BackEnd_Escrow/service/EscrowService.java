package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.enums.NotificationType;
import com.ddiring.BackEnd_Escrow.kafka.NotificationProducer;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EscrowService {
    private final EscrowRepository escrowRepository;
    private final NotificationProducer notificationProducer;

    //에스크로 계좌 생성
    public CreateAccountResponse createAccount(String projectId) {
        String accountNumber = generateUniqueAccountNumber();

        Escrow escrow = Escrow.builder()
                .projectId(projectId)
                .account(accountNumber)
                .createdId("admin")
                .createdAt(LocalDateTime.now())
                .updatedId("admin")
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            escrowRepository.save(escrow);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorCode.DUPLICATE_ACCOUNT);
        }

        notificationProducer.sendNotification(
                List.of(1, 2, 3, 4, 5),
                NotificationType.INFORMATION.name(),
                "계좌가 생성되었습니다: " + accountNumber
        );

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