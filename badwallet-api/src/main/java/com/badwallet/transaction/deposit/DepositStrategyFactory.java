package com.badwallet.transaction.deposit;

import com.badwallet.domain.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DepositStrategyFactory {

    private final Map<PaymentMethod, DepositStrategy> strategies;

    public DepositStrategyFactory(List<DepositStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(DepositStrategy::method, Function.identity()));
    }

    public DepositStrategy resolve(PaymentMethod method) {
        DepositStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("Methode de depot non supportee : " + method);
        }
        return strategy;
    }
}
