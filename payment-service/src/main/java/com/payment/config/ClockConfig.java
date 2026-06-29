package com.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock(@Value("${app.reference-date:2026-06-29T12:00:00Z}") String referenceDate) {
        return Clock.fixed(Instant.parse(referenceDate), ZoneOffset.UTC);
    }
}
