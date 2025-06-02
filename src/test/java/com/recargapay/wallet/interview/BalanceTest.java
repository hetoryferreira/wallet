package com.recargapay.wallet.interview;

import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.exception.NotFoundException;
import com.recargapay.wallet.interview.repository.BalanceRepository;
import com.recargapay.wallet.interview.response.BalanceResponse;
import com.recargapay.wallet.interview.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BalanceTest {

    private BalanceRepository balanceRepository;
    private BalanceService balanceService;

    @BeforeEach
    void setup() {
        balanceRepository = mock(BalanceRepository.class);
        balanceService = new BalanceService(balanceRepository);
    }

    @Test
    @DisplayName("Should return BalanceResponse when wallet exists (getBalanceByWalletId)")
    void getBalanceByWalletId_shouldReturnResponse_whenWalletExists() {
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50);
        BalanceEntity entity = mock(BalanceEntity.class);

        when(entity.getWalletId()).thenReturn(walletId);
        when(entity.getAmount()).thenReturn(amount);
        when(balanceRepository.findById(walletId)).thenReturn(Optional.of(entity));

        BalanceResponse response = balanceService.getBalanceByWalletId(walletId);

        assertNotNull(response);
        assertEquals(walletId, response.walletId());
        assertEquals(amount, response.amount());
        verify(balanceRepository, times(1)).findById(walletId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when wallet does not exist (getBalanceByWalletId)")
    void getBalanceByWalletId_shouldThrow_whenWalletNotFound() {
        Long walletId = 999L;
        when(balanceRepository.findById(walletId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> balanceService.getBalanceByWalletId(walletId)
        );
        assertTrue(ex.getMessage().contains(walletId.toString()));
        verify(balanceRepository, times(1)).findById(walletId);
    }

    @Test
    @DisplayName("Should return BalanceEntity when wallet exists (getBalanceEntityByWalletId)")
    void getBalanceEntityByWalletId_shouldReturnEntity_whenWalletExists() {
        Long walletId = 2L;
        BalanceEntity entity = mock(BalanceEntity.class);
        when(balanceRepository.findById(walletId)).thenReturn(Optional.of(entity));

        BalanceEntity found = balanceService.getBalanceEntityByWalletId(walletId);

        assertNotNull(found);
        verify(balanceRepository, times(1)).findById(walletId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when wallet does not exist (getBalanceEntityByWalletId)")
    void getBalanceEntityByWalletId_shouldThrow_whenWalletNotFound() {
        Long walletId = 888L;
        when(balanceRepository.findById(walletId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> balanceService.getBalanceEntityByWalletId(walletId)
        );
        assertTrue(ex.getMessage().contains(walletId.toString()));
        verify(balanceRepository, times(1)).findById(walletId);
    }

    @Test
    @DisplayName("Should call repository save when saving a balance entity")
    void save_shouldCallRepositorySave() {
        Long walletId = 3L;
        BigDecimal amount = BigDecimal.valueOf(200);
        BalanceEntity entity = mock(BalanceEntity.class);
        when(entity.getWalletId()).thenReturn(walletId);
        when(entity.getAmount()).thenReturn(amount);
        when(balanceRepository.save(entity)).thenReturn(entity);

        balanceService.save(entity);

        verify(balanceRepository, times(1)).save(entity);
    }
}