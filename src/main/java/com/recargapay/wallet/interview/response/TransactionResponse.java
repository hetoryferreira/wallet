package com.recargapay.wallet.interview.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.recargapay.wallet.interview.entity.TransactionType;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Response object containing transaction details")
public record TransactionResponse(
        @Schema(description = "Transaction ID", example = "101")
        Long id,

        @Schema(description = "Wallet ID associated with the transaction", example = "1001")
        Long walletId,

        @Schema(description = "Transaction amount", example = "150.75")
        BigDecimal amount,

        @Schema(description = "Transaction timestamp", example = "2024-06-01T15:00:00")
        LocalDateTime created,

        @Schema(description = "Transaction type (e.g., DEPOSIT, WITHDRAW, TRANSFER)", example = "DEPOSIT")
        TransactionType type
) {}
