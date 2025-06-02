package com.recargapay.wallet.interview.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record WalletRequest(@NotBlank String documentNumber) {

}