package com.recargapay.wallet.interview;

import com.recargapay.wallet.interview.entity.BalanceEntity;
import com.recargapay.wallet.interview.entity.WalletEntity;
import com.recargapay.wallet.interview.exception.AlreadyExistsException;
import com.recargapay.wallet.interview.exception.NotFoundException;
import com.recargapay.wallet.interview.repository.WalletRepository;
import com.recargapay.wallet.interview.response.WalletResponse;
import com.recargapay.wallet.interview.service.BalanceService;
import com.recargapay.wallet.interview.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("WalletService tests")
public class WalletTest {

    private WalletRepository walletRepository;
    private BalanceService balanceService;
    private WalletService walletService;

    @BeforeEach
    void setup() {
        walletRepository = mock(WalletRepository.class);
        balanceService = mock(BalanceService.class);
        walletService = new WalletService(walletRepository, balanceService);
    }

    @Test
    @DisplayName("Should create wallet and initial balance when document does not exist")
    void createWallet_shouldSucceed_whenDocumentDoesNotExist() {
        String documentNumber = "12345678901";

        when(walletRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.empty());

        WalletEntity savedWallet = WalletEntity.builder()
                .id(1L)
                .documentNumber(documentNumber)
                .createdAt(LocalDateTime.now())
                .build();
        when(walletRepository.save(any(WalletEntity.class))).thenReturn(savedWallet);

        WalletResponse response = walletService.createWallet(documentNumber);

        assertNotNull(response);
        assertEquals(savedWallet.getId(), response.id());
        assertEquals(savedWallet.getDocumentNumber(), response.documentNumber());
        verify(walletRepository).findByDocumentNumber(documentNumber);
        verify(walletRepository).save(any(WalletEntity.class));
        verify(balanceService).save(any(BalanceEntity.class));
    }

    @Test
    @DisplayName("Should throw AlreadyExistsException when wallet already exists for document")
    void createWallet_shouldThrow_whenDocumentAlreadyExists() {
        String documentNumber = "98765432100";
        WalletEntity existingWallet = WalletEntity.builder()
                .id(2L)
                .documentNumber(documentNumber)
                .createdAt(LocalDateTime.now())
                .build();

        when(walletRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.of(existingWallet));

        assertThrows(AlreadyExistsException.class, () ->
                walletService.createWallet(documentNumber)
        );
        verify(walletRepository).findByDocumentNumber(documentNumber);
        verify(walletRepository, never()).save(any());
        verify(balanceService, never()).save(any());
    }

    @Test
    @DisplayName("Should return WalletResponse when wallet is found by document number")
    void getWalletByDocumentNumber_shouldReturnWallet_whenExists() {
        String documentNumber = "55555555555";
        WalletEntity walletEntity = WalletEntity.builder()
                .id(10L)
                .documentNumber(documentNumber)
                .createdAt(LocalDateTime.now())
                .build();

        when(walletRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.of(walletEntity));

        WalletResponse response = walletService.getWalletByDocumentNumber(documentNumber);

        assertNotNull(response);
        assertEquals(walletEntity.getId(), response.id());
        assertEquals(walletEntity.getDocumentNumber(), response.documentNumber());
        verify(walletRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Should throw NotFoundException when wallet is not found by document number")
    void getWalletByDocumentNumber_shouldThrow_whenNotFound() {
        String documentNumber = "00000000000";
        when(walletRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                walletService.getWalletByDocumentNumber(documentNumber)
        );
        verify(walletRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Should return WalletResponse when wallet is found by walletId")
    void getWalletByWalletId_shouldReturnWallet_whenExists() {
        Long walletId = 22L;
        WalletEntity walletEntity = WalletEntity.builder()
                .id(walletId)
                .documentNumber("22222222222")
                .createdAt(LocalDateTime.now())
                .build();

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(walletEntity));

        WalletResponse response = walletService.getWalletById(walletId);

        assertNotNull(response);
        assertEquals(walletEntity.getId(), response.id());
        assertEquals(walletEntity.getDocumentNumber(), response.documentNumber());
        verify(walletRepository).findById(walletId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when wallet is not found by walletId")
    void getWalletByWalletId_shouldThrow_whenNotFound() {
        Long walletId = 333L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                walletService.getWalletById(walletId)
        );
        verify(walletRepository).findById(walletId);
    }
}