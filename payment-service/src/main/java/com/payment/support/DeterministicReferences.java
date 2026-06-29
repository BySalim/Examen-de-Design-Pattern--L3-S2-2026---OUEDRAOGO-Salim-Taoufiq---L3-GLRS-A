package com.payment.support;

import com.payment.domain.ServiceName;
import org.springframework.stereotype.Component;

@Component
public class DeterministicReferences {

    public String walletCode(int index) {
        return "WLT-%07d".formatted(index);
    }

    public String factureReference(ServiceName service, int walletIndex, int sequence) {
        return "FAC-%s-%d-%d".formatted(service.name(), walletIndex, sequence);
    }
}
