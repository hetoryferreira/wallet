package com.recargapay.wallet.interview.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class BalanceEntity {
    @Id
    private Long walletId; // reference as Wallet.id
    private BigDecimal amount;
}
