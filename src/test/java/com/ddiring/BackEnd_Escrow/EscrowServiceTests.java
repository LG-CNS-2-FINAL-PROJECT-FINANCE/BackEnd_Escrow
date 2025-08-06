package com.ddiring.BackEnd_Escrow;

import com.ddiring.BackEnd_Escrow.common.exception.ApplicationException;
import com.ddiring.BackEnd_Escrow.common.exception.ErrorCode;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.service.EscrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("에스크로 서비스 단위 테스트")
public class EscrowServiceTests {
    private EscrowRepository escrowRepository;
    private EscrowService escrowService;

    @BeforeEach
    void setUp() {
        escrowRepository = mock(EscrowRepository.class);
        escrowService = new EscrowService(escrowRepository);
    }

    @Test
    @DisplayName("계좌 생성")
    void createAccountTest() {
        when(escrowRepository.existsByAccount(anyString())).thenReturn(false);

        when(escrowRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateAccountResponse response = escrowService.createAccount(1, "user1");

        assertNotNull(response);
        assertNotNull(response.getAccount());
        assertEquals(14, response.getAccount().length());
    }

    @Test
    @DisplayName("계좌 생성 시 중복 에러")
    void createAccount_중복() {
        when(escrowRepository.existsByAccount(anyString())).thenReturn(false);
        when(escrowRepository.save(any()))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("duplicate"));

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> escrowService.createAccount(1, "user1"));

        assertEquals(ErrorCode.DUPLICATE_ACCOUNT.code(), ex.getErrorCode());
    }
}