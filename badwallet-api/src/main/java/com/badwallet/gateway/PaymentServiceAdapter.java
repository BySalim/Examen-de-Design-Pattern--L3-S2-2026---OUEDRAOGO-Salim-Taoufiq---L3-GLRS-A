package com.badwallet.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class PaymentServiceAdapter implements BillingGateway {

    private static final ParameterizedTypeReference<List<RemoteFacture>> LISTE_FACTURES =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    public PaymentServiceAdapter(@Value("${payment-service.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public List<RemoteFacture> facturesCourantes(String walletCode, String serviceName) {
        return restClient.get()
                .uri(builder -> builder.path("/internal/factures/{walletCode}/current")
                        .queryParam("unite", serviceName)
                        .build(walletCode))
                .retrieve()
                .body(LISTE_FACTURES);
    }

    @Override
    public List<RemoteFacture> facturesParReferences(String walletCode, List<String> references) {
        return restClient.get()
                .uri(builder -> builder.path("/internal/factures/{walletCode}/by-references")
                        .queryParam("references", references.toArray())
                        .build(walletCode))
                .retrieve()
                .body(LISTE_FACTURES);
    }

    @Override
    public void marquerPayees(String walletCode, List<String> references) {
        restClient.post()
                .uri("/internal/factures/pay")
                .body(Map.of("walletCode", walletCode, "references", references))
                .retrieve()
                .toBodilessEntity();
    }
}
