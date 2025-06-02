package com.recargapay.wallet.interview.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Response containing the wallet ID and its current balance")
public record BalanceResponse(
        @Schema(description = "Wallet ID", example = "123")
        Long walletId,

        @Schema(description = "Current wallet balance", example = "1000.00")
        BigDecimal amount
) {}