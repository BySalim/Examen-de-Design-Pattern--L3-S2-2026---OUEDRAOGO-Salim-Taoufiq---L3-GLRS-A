package com.payment.web.dto;

import java.util.List;

public record PayRequest(String walletCode, List<String> references) {
}
