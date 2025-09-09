package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.enums.NotificationType;
import com.ddiring.BackEnd_Escrow.kafka.EventEnvelope;
import com.ddiring.BackEnd_Escrow.kafka.NotificationPayload;
import com.ddiring.BackEnd_Escrow.kafka.NotificationProducer;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EscrowService {
    private final EscrowRepository escrowRepository;
    private final NotificationProducer notificationProducer;

    //에스크로 계좌 생성
    public CreateAccountResponse createAccount(String projectId) {
        //이미 계좌가 존재하면 조회만 하고 새로 만들지 않음
        Escrow existing = escrowRepository.findByProjectId(projectId).orElse(null);
        if (existing != null) {
            return CreateAccountResponse.fromEntity(existing);
        }

        //없으면 새로 생성
        String accountNumber = generateUniqueAccountNumber();
        Escrow escrow = Escrow.builder()
                .projectId(projectId)
                .account(accountNumber)
                .createdId("admin")
                .createdAt(LocalDateTime.now())
                .updatedId("admin")
                .updatedAt(LocalDateTime.now())
                .build();
        escrowRepository.save(escrow);

        //트랜잭션 바깥으로 FCM/Kafka 전송 분리
        //sendNotificationAsync(escrow);

        return CreateAccountResponse.fromEntity(escrow);
    }

    private void sendNotificationAsync(Escrow escrow) {
        List<String> userSeqList = List.of("3");
        notificationProducer.sendNotification(
                userSeqList,
                NotificationType.INFORMATION.name(),
                "에스크로 계좌 생성",
                "에스크로 계좌가 생성되었습니다.333333: " + escrow.getAccount()
        );
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