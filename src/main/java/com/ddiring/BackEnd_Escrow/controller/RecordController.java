package com.ddiring.BackEnd_Escrow.controller;

import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.BalanceResponse;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.service.RecordService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/escrow")
public class RecordController {
    private final RecordService recordService;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody SaveRecordRequest saveRecordRequest) {
        recordService.saveRecord(saveRecordRequest);

        return  ResponseEntity.ok("입금 성공");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody SaveRecordRequest saveRecordRequest) {
        recordService.saveRecord(saveRecordRequest);

        return  ResponseEntity.ok("출금 성공");
    }

    @GetMapping("/{escrowSeq}/history")
    public ResponseEntity<List<HistoryResponse>> getRecord(@PathVariable("escrowSeq") Integer escrowSeq) {
        List<HistoryResponse> response = recordService.getRecordsByEscrowSeq(escrowSeq);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{escrowSeq}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable("escrowSeq") Integer escrowSeq) {
       BalanceResponse response = recordService.getBalanceByEscrowSeq(escrowSeq);
        return ResponseEntity.ok(response);
    }

}
