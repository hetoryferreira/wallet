package com.recargapay.wallet.interview.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record BalanceRequest(Long walletId) {}