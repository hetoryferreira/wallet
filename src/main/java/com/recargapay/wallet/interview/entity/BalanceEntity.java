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
@Table(name = "balance")
public class BalanceEntity {
    @Id
    private Long walletId; // reference as Wallet.id
    private BigDecimal amount;
}
