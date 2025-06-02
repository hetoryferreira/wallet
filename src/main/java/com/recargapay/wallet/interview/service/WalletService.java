package com.recargapay.wallet.interview.service;

import com.recargapay.wallet.interview.response.WalletResponse;
import com.recargapay.wallet.interview.exception.AlreadyExistsException;
import com.recargapay.wallet.interview.exception.NotFoundException;
import com.recargapay.wallet.interview.entity.WalletEntity;
import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final BalanceService balanceService;

    @Transactional
    public WalletResponse createWallet(final String documentNumber) {
        log.info("Creating wallet and balance for documentNumber '{}'", documentNumber);

        walletRepository.findByDocumentNumber(documentNumber).ifPresent(w -> {
            log.info("Wallet already exists for documentNumber '{}'", documentNumber);
            throw new AlreadyExistsException("Wallet already exists for documentNumber " + documentNumber);
        });

        val wallet = WalletEntity.builder()
                .documentNumber(documentNumber)
                .createdAt(LocalDateTime.now())
                .build();

        WalletEntity savedWallet = walletRepository.save(wallet);

        val balanceEntity = BalanceEntity.builder()
                .amount(BigDecimal.ZERO)
                .walletId(savedWallet.getId())
                .build();

        balanceService.save(balanceEntity);

        log.info("Wallet and initial balance created for documentNumber '{}', walletId={}", documentNumber, savedWallet.getId());

         return WalletResponse.builder()
                 .id(savedWallet.getId())
                 .documentNumber(savedWallet.getDocumentNumber())
                 .build();
    }

    public WalletResponse getWalletByDocumentNumber(final String documentNumber) {
        log.info("Fetching wallet by documentNumber '{}'", documentNumber);

        val walletEntity = walletRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> {
                    log.warn("Wallet not found for documentNumber '{}'", documentNumber);
                    return new NotFoundException("Wallet not found for document " + documentNumber);
                });

        log.debug("Wallet found: id={}, documentNumber={}", walletEntity.getId(), walletEntity.getDocumentNumber());

        return WalletResponse.builder()
                .id(walletEntity.getId())
                .documentNumber(walletEntity.getDocumentNumber())
                .build();
    }

    public WalletResponse getWalletByWalletId(final Long walletId) {
        log.info("Fetching wallet by walletId '{}'", walletId);

        val walletEntity = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.warn("Wallet not found for walletId '{}'", walletId);
                    return new NotFoundException("Wallet not found for walletId " + walletId);
                });

        log.debug("Wallet found: id={}, documentNumber={}", walletEntity.getId(), walletEntity.getDocumentNumber());

        return WalletResponse.builder()
                .id(walletEntity.getId())
                .documentNumber(walletEntity.getDocumentNumber())
                .build();
    }
}