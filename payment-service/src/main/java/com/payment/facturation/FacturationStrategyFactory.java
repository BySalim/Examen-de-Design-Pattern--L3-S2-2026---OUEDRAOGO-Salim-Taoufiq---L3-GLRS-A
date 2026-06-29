package com.payment.facturation;

import com.payment.domain.ServiceName;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FacturationStrategyFactory {

    private final Map<ServiceName, FacturationStrategy> strategies;

    public FacturationStrategyFactory(List<FacturationStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(FacturationStrategy::service, Function.identity()));
    }

    public FacturationStrategy resolve(ServiceName service) {
        FacturationStrategy strategy = strategies.get(service);
        if (strategy == null) {
            throw new IllegalArgumentException("Service de facturation non supporte : " + service);
        }
        return strategy;
    }

    public Collection<FacturationStrategy> all() {
        return strategies.values();
    }
}
