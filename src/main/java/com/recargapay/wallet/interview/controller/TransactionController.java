package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.entity.TransactionType;
import com.recargapay.wallet.interview.request.TransactionRequest;
import com.recargapay.wallet.interview.request.TransferRequest;
import com.recargapay.wallet.interview.response.TransactionResponse;
import com.recargapay.wallet.interview.service.TransactionService;
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

    @PostMapping("/deposit")
    public  ResponseEntity<Void> deposit(@RequestBody TransactionRequest request) {
        log.info("Deposit {} to wallet '{}'", request.getAmount(), request.getWalletId());
        transactionService.deposit(request.getWalletId(), request.getAmount(),TransactionType.DEPOSIT);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody TransactionRequest request) {
        log.info("Withdraw {} from wallet '{}'", request.getAmount(), request.getWalletId());
        transactionService.withdraw(request.getWalletId(), request.getAmount(), TransactionType.WITHDRAW);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        log.info("Transfer {} from wallet '{}' to wallet '{}'",
                request.getAmount(), request.getFromWalletId(), request.getToWalletId());
        transactionService.transfer(request.getFromWalletId(), request.getToWalletId(), request.getAmount());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/wallet/{walletId}/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionsHistory(
            @PathVariable Long walletId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        log.info("API: Get transactions for walletId='{}' between '{}' and '{}'", walletId, start, end);
        return ResponseEntity.ok(transactionService.getTransactionsHistory(walletId, start, end));
    }
}
