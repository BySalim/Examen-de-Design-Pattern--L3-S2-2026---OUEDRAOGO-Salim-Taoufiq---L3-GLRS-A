package com.badwallet.support;

import org.springframework.stereotype.Component;

@Component
public class DeterministicReferences {

    public String phone(int index) {
        return "+22177%07d".formatted(index);
    }

    public String walletCode(int index) {
        return "WLT-%07d".formatted(index);
    }
}
