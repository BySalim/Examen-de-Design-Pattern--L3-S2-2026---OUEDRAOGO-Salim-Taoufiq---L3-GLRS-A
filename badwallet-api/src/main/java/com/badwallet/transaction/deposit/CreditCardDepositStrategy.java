package com.badwallet.transaction.deposit;

import com.badwallet.domain.PaymentMethod;
import com.badwallet.domain.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditCardDepositStrategy implements DepositStrategy {

    @Override
    public PaymentMethod method() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public String appliquer(Wallet wallet, BigDecimal amount) {
        wallet.crediter(amount);
        return "Depot par carte de credit";
    }
}
