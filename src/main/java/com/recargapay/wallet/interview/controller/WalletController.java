package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.response.WalletResponse;
import com.recargapay.wallet.interview.request.WalletRequest;
import com.recargapay.wallet.interview.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody WalletRequest request) {
        log.info("API: Create wallet for documentNumber='{}'", request.documentNumber());

        WalletResponse wallet = walletService.createWallet(request.documentNumber());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(wallet.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(wallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable Long walletId) {
        log.info("API: Get wallet by walletId '{}'", walletId);
        return ResponseEntity.ok(walletService.getWalletByWalletId(walletId));
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWalletByDocument(@RequestParam String documentNumber) {
        log.info("API: Get wallet by documentNumber '{}'", documentNumber);
        return ResponseEntity.ok(walletService.getWalletByDocumentNumber(documentNumber));
    }
}