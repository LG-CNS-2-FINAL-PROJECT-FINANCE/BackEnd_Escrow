package com.ddiring.BackEnd_Escrow.client;

import com.ddiring.BackEnd_Escrow.dto.request.DistributedIncomeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "distributedIncomeClient", url = "${external.product-service.url}")
public interface DistributedIncomeClient {

    @PostMapping("/api/product/escrow/distribution")
    void notifyDistributedIncome(@RequestBody DistributedIncomeRequest request);
}