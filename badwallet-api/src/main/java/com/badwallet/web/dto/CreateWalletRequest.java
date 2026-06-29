package com.badwallet.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateWalletRequest(

        @NotBlank String phoneNumber,

        @NotBlank @Email String email,

        @NotNull @PositiveOrZero BigDecimal initialBalance,

        @NotBlank String code,

        @NotBlank @Size(min = 3, max = 3) String currency
) {
}
