package com.recargapay.wallet.interview.service;

import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.response.BalanceResponse;
import com.recargapay.wallet.interview.exception.NotFoundException;
import com.recargapay.wallet.interview.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceResponse getBalanceByWalletId(final Long walletId) {
        log.info("Find balance for walletId={}", walletId);
        val balance = balanceRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.warn("Wallet not found for walletId={}", walletId);
                    return new NotFoundException("Wallet not found with id " + walletId);
                });

        log.debug("Balance found for walletId={}: {}", walletId, balance.getAmount());
        return new BalanceResponse(balance.getWalletId(), balance.getAmount());
    }

    public BalanceEntity getBalanceEntityByWalletId(Long walletId) {
        log.info("Find balance entity for walletId={}", walletId);
        return balanceRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.warn("Balance entity not found for walletId={}", walletId);
                    return new NotFoundException("Wallet not found with id " + walletId);
                });
    }

    @Transactional
    public void save(final BalanceEntity balanceEntity) {
        log.info("Saving balance for walletId={}, new amount={}",
                balanceEntity.getWalletId(), balanceEntity.getAmount());
        BalanceEntity saved = balanceRepository.save(balanceEntity);
        log.debug("Balance saved for walletId={}: {}", saved.getWalletId(), saved.getAmount());
    }
}