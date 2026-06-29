package com.badwallet.transaction.deposit;

import com.badwallet.domain.PaymentMethod;
import com.badwallet.domain.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletTargetDepositStrategy implements DepositStrategy {

    @Override
    public PaymentMethod method() {
        return PaymentMethod.WALLET_TARGET;
    }

    @Override
    public String appliquer(Wallet wallet, BigDecimal amount) {
        wallet.crediter(amount);
        return "Depot depuis un autre portefeuille";
    }
}
