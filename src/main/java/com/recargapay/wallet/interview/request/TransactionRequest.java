package com.recargapay.wallet.interview.request;

import com.recargapay.wallet.interview.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private Long id;
    private Long walletId;
    @Positive
    private BigDecimal amount;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

  public TransactionRequest(Long walletId , BigDecimal amount, LocalDateTime timestamp, TransactionType type){
          this.walletId =walletId;;
          this.timestamp= timestamp;
          this.amount = amount;
          this.type= type;
    }
}
