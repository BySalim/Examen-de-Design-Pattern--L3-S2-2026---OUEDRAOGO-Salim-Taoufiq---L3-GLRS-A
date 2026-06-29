package com.badwallet.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentSeedClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentSeedClient.class);

    private final RestClient restClient;

    public PaymentSeedClient(@Value("${payment-service.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public void declencherSeed(int numWallets) {
        try {
            restClient.post()
                    .uri(builder -> builder.path("/internal/seed").queryParam("numWallets", numWallets).build())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception exception) {
            log.warn("Seed du payment-service indisponible : {}", exception.getMessage());
        }
    }
}
