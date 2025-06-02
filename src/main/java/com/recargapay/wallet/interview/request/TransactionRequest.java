package com.recargapay.wallet.interview.request;

import com.recargapay.wallet.interview.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionRequest(
        Long walletId,
        @Positive BigDecimal amount
) {}