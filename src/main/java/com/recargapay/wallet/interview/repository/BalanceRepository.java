package com.recargapay.wallet.interview.repository;


import com.recargapay.wallet.interview.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {}

