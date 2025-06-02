package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.entity.TransactionType;
import com.recargapay.wallet.interview.request.TransactionRequest;
import com.recargapay.wallet.interview.request.TransferRequest;
import com.recargapay.wallet.interview.response.TransactionResponse;
import com.recargapay.wallet.interview.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Deposit funds into a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deposit successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Deposit request details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionRequest.class))
            )
            @RequestBody TransactionRequest request) {
        log.info("Deposit {} to wallet '{}'", request.amount(), request.walletId());
        transactionService.deposit(request.walletId(), request.amount(), TransactionType.DEPOSIT);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Withdraw funds from a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Withdraw request details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionRequest.class))
            )
            @RequestBody TransactionRequest request) {
        log.info("Withdraw {} from wallet '{}'", request.amount(), request.walletId());
        transactionService.withdraw(request.walletId(), request.amount(), TransactionType.WITHDRAW);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Transfer funds between wallets",
            description = "Transfers a specified amount from one wallet to another."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or transfer to same wallet"),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transfer request details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransferRequest.class))
            )
            @RequestBody @Valid TransferRequest request) {
        log.info("Transfer {} from wallet '{}' to wallet '{}'",
                request.amount(), request.fromWalletId(), request.toWalletId());
        transactionService.transfer(request.fromWalletId(), request.toWalletId(), request.amount());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get wallet transaction history",
            description = "Returns a list of transactions for a specific wallet, optionally filtered by date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/wallet/{walletId}/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionsHistory(
            @Parameter(description = "ID of the wallet", required = true, example = "123")
            @PathVariable Long walletId,
            @Parameter(description = "Start date and time (yyyy-MM-dd HH:mm:ss)", example = "2024-06-01 00:00:00")
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @Parameter(description = "End date and time (yyyy-MM-dd HH:mm:ss)", example = "2024-06-01 23:59:59")
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        log.info("API: Get transactions for walletId='{}' between '{}' and '{}'", walletId, start, end);
        return ResponseEntity.ok(transactionService.getTransactionsHistory(walletId, start, end));
    }
}