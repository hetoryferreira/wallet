package com.recargapay.wallet.interview.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "transaction",
        indexes = {
                @Index(name = "idx_wallet_id", columnList = "walletId"),
                @Index(name = "idx_wallet_created", columnList = "walletId, created")
        }
)
public class TransactionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
