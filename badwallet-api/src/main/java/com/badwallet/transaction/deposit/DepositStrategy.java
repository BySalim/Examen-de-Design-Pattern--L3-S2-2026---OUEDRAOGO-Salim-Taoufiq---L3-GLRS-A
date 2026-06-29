package com.badwallet.transaction.deposit;

import com.badwallet.domain.PaymentMethod;
import com.badwallet.domain.Wallet;

import java.math.BigDecimal;

public interface DepositStrategy {

    PaymentMethod method();

    String appliquer(Wallet wallet, BigDecimal amount);
}
