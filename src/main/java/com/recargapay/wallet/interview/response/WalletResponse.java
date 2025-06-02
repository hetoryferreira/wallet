package com.recargapay.wallet.interview.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Response object containing wallet details")
public record WalletResponse(
        @Schema(description = "Wallet ID", example = "1001")
        Long id,

        @Schema(description = "Wallet owner's document number", example = "12345678901")
        String documentNumber,

        @Schema(description = "Wallet creation timestamp", example = "2024-06-01T15:00:00")
        LocalDateTime createdAt
) {
}
