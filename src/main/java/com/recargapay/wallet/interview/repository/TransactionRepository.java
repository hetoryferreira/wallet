package com.recargapay.wallet.interview.repository;

import com.recargapay.wallet.interview.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByWalletId(Long walletId);

    List<TransactionEntity> findByWalletIdAndCreatedBetween(Long walletId, LocalDateTime start, LocalDateTime end);

    List<TransactionEntity> findByWalletIdAndCreatedGreaterThanEqual(Long walletId, LocalDateTime start);

    List<TransactionEntity> findByWalletIdAndCreatedLessThanEqual(Long walletId, LocalDateTime end);
}