package com.badwallet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawRequest(

        @NotBlank String phoneNumber,

        @NotNull @Positive BigDecimal amount
) {
}
