package com.recargapay.wallet.interview.request;

import com.recargapay.wallet.interview.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransferRequest extends TransactionRequest{
    @NotNull(message = "The document number must not be null")
    @NotBlank(message = "The document number must not be blank")
    private Long  fromWalletId;

    @NotNull(message = "The document number must not be null")
    @NotBlank(message = "The document number must not be blank")
    private Long toWalletId;
}
