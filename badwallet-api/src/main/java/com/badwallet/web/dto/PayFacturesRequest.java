package com.badwallet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PayFacturesRequest(

        @NotBlank String phoneNumber,

        @NotBlank String serviceName,

        @NotEmpty List<String> factureReferences
) {
}
