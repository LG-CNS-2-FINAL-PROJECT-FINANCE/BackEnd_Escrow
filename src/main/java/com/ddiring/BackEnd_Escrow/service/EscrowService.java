package com.ddiring.BackEnd_Escrow.service;

import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import lombok.RequiredArgsConstructor;
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
        Escrow escrow = Escrow.builder()
                .projectSeq(projectSeq)
                .account(generateAccountNumber())
                .createdId(userSeq)
                .createdAt(LocalDateTime.now())
                .updatedId(userSeq)
                .updatedAt(LocalDateTime.now())
                .build();

        escrowRepository.save(escrow);

        return CreateAccountResponse.fromEntity(escrow);
    }

    //계좌번호 생성
    private String generateAccountNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); //ex) 20250730

        int randomPart = new Random().nextInt(900000) + 100000; //6자리 난수
        return datePart + randomPart; //ex) 20250730123456 (14자리)
    }
}