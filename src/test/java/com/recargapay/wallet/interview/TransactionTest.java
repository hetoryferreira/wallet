package com.recargapay.wallet.interview;

import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.entity.TransactionEntity;
import com.recargapay.wallet.interview.entity.TransactionType;
import com.recargapay.wallet.interview.exception.BadRequestException;
import com.recargapay.wallet.interview.repository.TransactionRepository;
import com.recargapay.wallet.interview.response.BalanceResponse;
import com.recargapay.wallet.interview.response.TransactionResponse;
import com.recargapay.wallet.interview.service.BalanceService;
import com.recargapay.wallet.interview.service.TransactionService;
import com.recargapay.wallet.interview.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionTest {

    private BalanceService balanceService;
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        balanceService = mock(BalanceService.class);
        transactionRepository = mock(TransactionRepository.class);
        walletService = mock(WalletService.class);
        transactionService = new TransactionService(balanceService, transactionRepository,walletService);
    }

    @Test
    @DisplayName("Deposit should throw BadRequestException for negative amount")
    void deposit_shouldThrowException_forNegativeAmount() {
        assertThrows(BadRequestException.class, () ->
                transactionService.deposit(1L, BigDecimal.valueOf(-100), TransactionType.DEPOSIT)
        );
        verify(balanceService, never()).getBalanceEntityByWalletId(anyLong());
    }

    @Test
    @DisplayName("Withdraw should throw BadRequestException for negative amount")
    void withdraw_shouldThrowException_forNegativeAmount() {
        assertThrows(BadRequestException.class, () ->
                transactionService.withdraw(1L, BigDecimal.valueOf(-100), TransactionType.WITHDRAW)
        );
        verify(balanceService, never()).getBalanceEntityByWalletId(anyLong());
    }

    @Test
    @DisplayName("Withdraw should throw BadRequestException for insufficient funds")
    void withdraw_shouldThrowException_forInsufficientFunds() {
        BigDecimal initial = BigDecimal.valueOf(10);
        when(balanceService.getBalanceByWalletId(1L)).thenReturn(new BalanceResponse(1L, initial));
        assertThrows(BadRequestException.class, () ->
                transactionService.withdraw(1L, BigDecimal.valueOf(20), TransactionType.WITHDRAW)
        );
    }

    @Test
    @DisplayName("Transfer should succeed for valid different wallet IDs and positive amount")
    void transfer_shouldCallDepositAndWithdraw_forValidAmount() {
        TransactionService spyService = spy(transactionService);
        doNothing().when(spyService).withdraw(anyLong(), any(), any());
        doNothing().when(spyService).deposit(anyLong(), any(), any());

        spyService.transfer(1L, 2L, BigDecimal.TEN);

        verify(spyService).withdraw(1L, BigDecimal.TEN, TransactionType.TRANSFER_OUT);
        verify(spyService).deposit(2L, BigDecimal.TEN, TransactionType.TRANSFER_IN);
    }

    @Test
    @DisplayName("Transfer should throw BadRequestException for same wallet IDs")
    void transfer_shouldThrowException_forSameWalletId() {
        assertThrows(BadRequestException.class, () ->
                transactionService.transfer(1L, 1L, BigDecimal.TEN)
        );
    }

    @Test
    @DisplayName("Transfer should throw BadRequestException for negative amount")
    void transfer_shouldThrowException_forNegativeAmount() {
        assertThrows(BadRequestException.class, () ->
                transactionService.transfer(1L, 2L, BigDecimal.valueOf(-5))
        );
    }

    @Test
    @DisplayName("Should call findByWalletIdAndCreatedBetween when both start and end are present")
    void getTransactionsHistory_startAndEndPresent_callsFindByWalletIdAndCreatedBetween() {
        Long walletId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now();
        TransactionEntity entity = TransactionEntity.builder()
                .id(10L)
                .walletId(walletId)
                .amount(BigDecimal.ONE)
                .created(start.plusDays(1))
                .type(TransactionType.DEPOSIT)
                .build();
        when(transactionRepository.findByWalletIdAndCreatedBetween(walletId, start, end))
                .thenReturn(List.of(entity));

        List<TransactionResponse> responses = transactionService.getTransactionsHistory(walletId, start, end);

        assertEquals(1, responses.size());
        assertEquals(entity.getId(), responses.get(0).id());
        verify(transactionRepository, times(1)).findByWalletIdAndCreatedBetween(walletId, start, end);
    }

    @Test
    @DisplayName("Should call findByWalletIdAndCreatedGreaterThanEqual when only start is present")
    void getTransactionsHistory_startOnly_callsFindByWalletIdAndCreatedGreaterThanEqual() {
        Long walletId = 2L;
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        TransactionEntity entity = TransactionEntity.builder()
                .id(20L)
                .walletId(walletId)
                .amount(BigDecimal.TEN)
                .created(LocalDateTime.now())
                .type(TransactionType.WITHDRAW)
                .build();
        when(transactionRepository.findByWalletIdAndCreatedGreaterThanEqual(walletId, start))
                .thenReturn(List.of(entity));

        List<TransactionResponse> responses = transactionService.getTransactionsHistory(walletId, start, null);

        assertEquals(1, responses.size());
        assertEquals(entity.getId(), responses.get(0).id());
        verify(transactionRepository, times(1)).findByWalletIdAndCreatedGreaterThanEqual(walletId, start);
    }

    @Test
    @DisplayName("Should call findByWalletIdAndCreatedLessThanEqual when only end is present")
    void getTransactionsHistory_endOnly_callsFindByWalletIdAndCreatedLessThanEqual() {
        Long walletId = 3L;
        LocalDateTime end = LocalDateTime.now();
        TransactionEntity entity = TransactionEntity.builder()
                .id(30L)
                .walletId(walletId)
                .amount(BigDecimal.valueOf(5))
                .created(LocalDateTime.now().minusHours(1))
                .type(TransactionType.TRANSFER_IN)
                .build();
        when(transactionRepository.findByWalletIdAndCreatedLessThanEqual(walletId, end))
                .thenReturn(List.of(entity));

        List<TransactionResponse> responses = transactionService.getTransactionsHistory(walletId, null, end);

        assertEquals(1, responses.size());
        assertEquals(entity.getId(), responses.get(0).id());
        verify(transactionRepository, times(1)).findByWalletIdAndCreatedLessThanEqual(walletId, end);
    }

    @Test
    @DisplayName("Should call findByWalletId when neither start nor end are present")
    void getTransactionsHistory_noStartNoEnd_callsFindByWalletId() {
        Long walletId = 4L;
        TransactionEntity entity = TransactionEntity.builder()
                .id(40L)
                .walletId(walletId)
                .amount(BigDecimal.valueOf(2))
                .created(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .build();
        when(transactionRepository.findByWalletId(walletId))
                .thenReturn(List.of(entity));

        List<TransactionResponse> responses = transactionService.getTransactionsHistory(walletId, null, null);

        assertEquals(1, responses.size());
        assertEquals(entity.getId(), responses.get(0).id());
        verify(transactionRepository, times(1)).findByWalletId(walletId);
    }
}
