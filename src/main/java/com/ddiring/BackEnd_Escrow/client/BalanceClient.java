package com.ddiring.BackEnd_Escrow.client;

import com.ddiring.BackEnd_Escrow.dto.request.BalanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "balanceClient", url = "${external.product-service.url}")
public interface BalanceClient {
    @PostMapping("/api/product/escrow/balance")
    void sendBalance(@RequestBody BalanceRequest request);
}