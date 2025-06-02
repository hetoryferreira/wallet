package com.recargapay.wallet.interview.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "The source walletId (fromWalletId) must not be null")
        Long fromWalletId,

        @NotNull(message = "The destination walletId (toWalletId) must not be null")
        Long toWalletId,

        BigDecimal amount
) {}