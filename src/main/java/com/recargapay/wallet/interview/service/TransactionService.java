package com.recargapay.wallet.interview.service;

import com.recargapay.wallet.interview.entity.WalletEntity;
import com.recargapay.wallet.interview.response.TransactionResponse;
import com.recargapay.wallet.interview.entity.TransactionEntity;
import com.recargapay.wallet.interview.entity.TransactionType;
import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.exception.BadRequestException;
import com.recargapay.wallet.interview.exception.NotFoundException;
import com.recargapay.wallet.interview.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class TransactionService {

    private final BalanceService balanceService;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Transactional
    public void deposit(final Long walletId, final BigDecimal amount, final TransactionType type) {
        log.info("Starting deposit of {} into walletId={}", amount, walletId);

        walletService.getWalletById(walletId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Deposit amount must be greater than 0");

        updateTransactionAndBalance(walletId, amount, type);

        log.info("Deposit of {} into walletId={} completed.", amount, walletId);
    }

    @Transactional
    public void withdraw(final Long walletId, final BigDecimal amount, final TransactionType type) {
        log.info("Starting withdrawal of {} from walletId={}", amount, walletId);

        walletService.getWalletById(walletId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Withdrawal amount must be greater than 0");

        val currentAmount = balanceService.getBalanceByWalletId(walletId).amount();
        if (currentAmount.compareTo(amount) < 0)
            throw new BadRequestException("Insufficient funds");

        updateTransactionAndBalance(walletId, amount.negate(), type);

        log.info("Withdrawal of {} from walletId={} completed.", amount, walletId);
    }

    @Transactional
    public void transfer(final Long fromWalletId, final Long toWalletId, final BigDecimal amount) {
        log.info("Starting transfer of {} from walletId={} to walletId={}", amount, fromWalletId, toWalletId);

        if (fromWalletId.equals(toWalletId)) {
            throw new BadRequestException("Cannot transfer to the same wallet.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Transfer amount must be greater than 0");

        withdraw(fromWalletId, amount, TransactionType.TRANSFER_OUT);
        deposit(toWalletId, amount, TransactionType.TRANSFER_IN);

        log.info("Transfer of {} from walletId={} to walletId={} completed.", amount, fromWalletId, toWalletId);
    }

    @Transactional
    private void updateTransactionAndBalance(final Long walletId, final BigDecimal amount, final TransactionType type) {
        log.info("Updating transaction and balance. walletId={}, amount={}, type={}", walletId, amount, type);

        BalanceEntity balanceEntity = balanceService.getBalanceEntityByWalletId(walletId);

        BigDecimal valueBalance = balanceEntity.getAmount();
        balanceEntity.setAmount(valueBalance.add(amount));

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        val tx = TransactionEntity.builder()
                .walletId(walletId)
                .amount(amount)
                .type(type)
                .created(now)
                .build();

        transactionRepository.save(tx);
        balanceService.save(balanceEntity);

        log.info("Transaction of type {} and amount {} saved for walletId={}. Old balance: {}, new balance: {}.",
                type, amount, walletId, valueBalance, balanceEntity.getAmount());
    }

    public List<TransactionResponse> getTransactionsHistory(final Long walletId, final LocalDateTime start, final LocalDateTime end) {
        log.info("Fetching transactions for walletId={}, start={}, end={}", walletId, start, end);

        List<TransactionEntity> entities;

        if (start != null && end != null) {
            entities = transactionRepository.findByWalletIdAndCreatedBetween(walletId, start, end);
        } else if (start != null) {
            entities = transactionRepository.findByWalletIdAndCreatedGreaterThanEqual(walletId, start);
        } else if (end != null) {
            entities = transactionRepository.findByWalletIdAndCreatedLessThanEqual(walletId, end);
        } else {
            entities = transactionRepository.findByWalletId(walletId);
        }

        log.info("Found {} transactions for walletId={}", entities.size(), walletId);

        return entities.stream()
                .map(entity -> TransactionResponse.builder()
                        .id(entity.getId())
                        .walletId(entity.getWalletId())
                        .amount(entity.getAmount())
                        .created(entity.getCreated())
                        .type(entity.getType())
                        .build()
                )
                .collect(Collectors.toList());
    }
}