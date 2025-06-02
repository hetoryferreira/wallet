package com.recargapay.wallet.interview.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(
        name = "wallet",
        indexes = {
                @Index(name = "idx_id", columnList = "id"),
                @Index(name = "idx_documentNumber", columnList = "documentNumber")
        }
)
public class WalletEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentNumber;
    private LocalDateTime createdAt;
}