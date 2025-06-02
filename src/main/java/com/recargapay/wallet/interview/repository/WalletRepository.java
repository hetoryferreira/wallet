package com.recargapay.wallet.interview.repository;

import com.recargapay.wallet.interview.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByDocumentNumber(String documentNumber);
}