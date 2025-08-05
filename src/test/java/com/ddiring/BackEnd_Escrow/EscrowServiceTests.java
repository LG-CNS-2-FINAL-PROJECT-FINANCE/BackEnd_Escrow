package com.ddiring.BackEnd_Escrow;

import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.repository.EscrowRepository;
import com.ddiring.BackEnd_Escrow.service.EscrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
    void createAccountTest() {
        CreateAccountResponse response = escrowService.createAccount(1, "user1");

        assertNotNull(response);

        assertNotNull(response.getAccount());
        assert(response.getAccount().length() == 14);
    }
}