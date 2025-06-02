package com.recargapay.wallet.interview.controller;

import com.recargapay.wallet.interview.response.WalletResponse;
import com.recargapay.wallet.interview.request.WalletRequest;
import com.recargapay.wallet.interview.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(
            summary = "Create a new wallet",
            description = "Creates a new wallet for the provided document number."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Wallet created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Wallet already exists")
    })
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(
            @RequestBody(
                    description = "Wallet creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WalletRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody WalletRequest request
    ) {
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

    @Operation(
            summary = "Get wallet by ID",
            description = "Fetches wallet details by wallet ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Wallet found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWalletById(
            @Parameter(description = "ID of the wallet", required = true, example = "123")
            @PathVariable Long walletId
    ) {
        log.info("API: Get wallet by walletId '{}'", walletId);
        return ResponseEntity.ok(walletService.getWalletById(walletId));
    }

    @Operation(
            summary = "Get wallet by document number",
            description = "Fetches wallet details by the document number."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Wallet found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<WalletResponse> getWalletByDocument(
            @Parameter(description = "Document number of the wallet", required = true, example = "12345678901")
            @RequestParam String documentNumber
    ) {
        log.info("API: Get wallet by documentNumber '{}'", documentNumber);
        return ResponseEntity.ok(walletService.getWalletByDocumentNumber(documentNumber));
    }
}
