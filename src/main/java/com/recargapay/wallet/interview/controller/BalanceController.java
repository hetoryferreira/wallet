package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.response.BalanceResponse;
import com.recargapay.wallet.interview.service.BalanceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @Operation(
            summary = "Get wallet balance",
            description = "Returns the current balance for a specific wallet."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Wallet balance retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BalanceResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
    })
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<BalanceResponse> getBalance(
            @Parameter(description = "ID of the wallet", required = true, example = "123")
            @PathVariable Long walletId
    ) {
        log.info("API: Get balance for wallet '{}'", walletId);
        return ResponseEntity.ok(balanceService.getBalanceByWalletId(walletId));
    }
}