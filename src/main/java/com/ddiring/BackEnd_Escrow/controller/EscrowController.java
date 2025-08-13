package com.ddiring.BackEnd_Escrow.controller;

import com.ddiring.BackEnd_Escrow.dto.request.CreateAccountRequest;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.service.EscrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/escrow")
public class EscrowController {
    private final EscrowService escrowService;

    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        CreateAccountResponse createAccountResponse = escrowService.createAccount(request.getProjectId());
        return ResponseEntity.ok(createAccountResponse);
    }
}