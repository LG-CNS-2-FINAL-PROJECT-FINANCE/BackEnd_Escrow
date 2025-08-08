package com.ddiring.BackEnd_Escrow.client;

import com.ddiring.BackEnd_Escrow.dto.request.BalanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "balanceClient", url = "http://localhost:8083")
public interface BalanceClient {
    @PostMapping("/api/balance")
    void sendBalance(@RequestBody BalanceRequest request);
}