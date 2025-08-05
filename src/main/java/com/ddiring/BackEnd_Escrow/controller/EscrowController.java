package com.ddiring.BackEnd_Escrow.controller;

import com.ddiring.BackEnd_Escrow.dto.request.CreateAccountRequest;
import com.ddiring.BackEnd_Escrow.dto.response.CreateAccountResponse;
import com.ddiring.BackEnd_Escrow.service.EscrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/escrow")
public class EscrowController {
    private final EscrowService escrowService;

    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        CreateAccountResponse createAccountResponse = escrowService.createAccount(request.getProjectSeq(), request.getUserSeq());
        return ResponseEntity.ok(createAccountResponse);
    }
}