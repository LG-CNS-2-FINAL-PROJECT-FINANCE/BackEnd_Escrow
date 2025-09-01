package com.ddiring.BackEnd_Escrow.controller;

import com.ddiring.BackEnd_Escrow.dto.request.DistributedIncomeRequest;
import com.ddiring.BackEnd_Escrow.dto.request.SaveRecordRequest;
import com.ddiring.BackEnd_Escrow.dto.response.BalanceResponse;
import com.ddiring.BackEnd_Escrow.dto.response.HistoryResponse;
import com.ddiring.BackEnd_Escrow.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/escrow")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody SaveRecordRequest request) {
        recordService.saveRecord(request);
        return ResponseEntity.ok().body("입금 성공");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody SaveRecordRequest request) {
        recordService.saveRecord(request);
        return ResponseEntity.ok().body("출금 성공");
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refund(@RequestBody SaveRecordRequest request) {
        recordService.saveRecord(request);
        return ResponseEntity.ok().body("환불 성공");
    }

    @GetMapping("/{projectId}/history")
    public ResponseEntity<List<HistoryResponse>> getHistory(@PathVariable String projectId) {
        List<HistoryResponse> response = recordService.getRecordsByProjectId(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String projectId) {
        BalanceResponse response = recordService.getBalance(null, projectId);
        return ResponseEntity.ok(response);
    }

}