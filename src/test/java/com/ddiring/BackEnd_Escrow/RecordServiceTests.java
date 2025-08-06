package com.ddiring.BackEnd_Escrow;

import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.entity.Escrow;
import com.ddiring.BackEnd_Escrow.entity.Record;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.repository.RecordRepository;
import com.ddiring.BackEnd_Escrow.service.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("레코드 서비스 단위 테스트")
public class RecordServiceTests {
    private EscrowRepository escrowRepository;
    private RecordRepository recordRepository;
    private RecordService recordService;

    @BeforeEach
    void setUp() {
        escrowRepository = mock(EscrowRepository.class);
        recordRepository = mock(RecordRepository.class);
        recordService = new RecordService(escrowRepository, recordRepository);
    }

    @Test
    @DisplayName("입출금 내역 저장")
    void saveRecordTest() {
        Integer escrowSeq = 1;
        Integer userSeq = 1001;
        Integer transSeq = 999;
        BigDecimal amount = new BigDecimal("100000");
        Integer transTypeCode = 1;

        Escrow testEscrow = Escrow.builder()
                .escrowSeq(escrowSeq)
                .projectSeq(123)
                .account("20250731302603")
                .createdAt(LocalDateTime.now())
                .build();

        when(escrowRepository.findByEscrowSeq(escrowSeq)).thenReturn(java.util.Optional.of(testEscrow));

        SaveRecordRequest request = new SaveRecordRequest(
                escrowSeq,
                userSeq,
                amount,
                transSeq,
                transTypeCode
        );


        // when
        recordService.saveRecord(request);

        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    @DisplayName("에스크로 입출금 내역 조회")
    void getRecordsByEscrowSeqTest() {
        Integer escrowSeq = 1;

        Record record1 = Record.builder()
                .recordSeq(1)
                .escrowSeq(escrowSeq)
                .flow(1)
                .amount(new BigDecimal("50000"))
                .createdAt(LocalDateTime.now())
                .build();

        Record record2 = Record.builder()
                .recordSeq(1)
                .escrowSeq(escrowSeq)
                .flow(0)
                .amount(new BigDecimal("5000"))
                .createdAt(LocalDateTime.now())
                .build();

        when(recordRepository.findAllByEscrowSeq(escrowSeq))
                .thenReturn(List.of(record1, record2));

        List<HistoryResponse> responses = recordService.getRecordsByEscrowSeq(escrowSeq);

        assertEquals(2, responses.size());
        assertEquals(1, responses.get(0).getFlow());
        assertEquals(new BigDecimal("50000"), responses.get(0).getAmount());
    }

    @Test
    @DisplayName("에스크로 번호로 잔액 조회")
    void getBalanceByEscrowSeqTest() {
        Integer escrowSeq = 1;
        BigDecimal testBalance = new BigDecimal("150000");

        when(recordRepository.findBalanceByEscrowSeq(escrowSeq)).thenReturn(testBalance);

        BigDecimal realBalance = recordService.getBalanceByEscrowSeq(escrowSeq);

        assertEquals(testBalance, realBalance);
        verify(recordRepository, times(1)).findBalanceByEscrowSeq(escrowSeq);
    }
}
