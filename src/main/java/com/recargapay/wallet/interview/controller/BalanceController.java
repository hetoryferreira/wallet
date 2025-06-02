package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.response.BalanceResponse;
import com.recargapay.wallet.interview.service.BalanceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<BalanceResponse> getBalance(
            @Parameter(description = "ID of the wallet", required = true, example = "123")
            @PathVariable Long walletId
    ) {
        log.info("API: Get balance for wallet '{}'", walletId);
        return ResponseEntity.ok(balanceService.getBalanceByWalletId(walletId));
    }
}