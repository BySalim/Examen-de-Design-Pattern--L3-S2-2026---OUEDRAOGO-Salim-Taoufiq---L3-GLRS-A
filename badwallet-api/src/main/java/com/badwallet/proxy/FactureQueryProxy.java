package com.badwallet.proxy;

import com.badwallet.gateway.RemoteFacture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;

@Component
public class FactureQueryProxy implements FactureQueryService {

    private static final ParameterizedTypeReference<List<RemoteFacture>> LISTE_FACTURES =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    public FactureQueryProxy(@Value("${payment-service.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public List<RemoteFacture> courantes(String walletCode, String unite) {
        return restClient.get()
                .uri(builder -> {
                    UriBuilder etape = builder.path("/internal/factures/{walletCode}/current");
                    if (unite != null && !unite.isBlank()) {
                        etape.queryParam("unite", unite);
                    }
                    return etape.build(walletCode);
                })
                .retrieve()
                .body(LISTE_FACTURES);
    }

    @Override
    public List<RemoteFacture> impayeesSurPeriode(String walletCode, String debut, String fin, String unite) {
        return restClient.get()
                .uri(builder -> {
                    UriBuilder etape = builder.path("/internal/factures/{walletCode}/periode")
                            .queryParam("debut", debut)
                            .queryParam("fin", fin);
                    if (unite != null && !unite.isBlank()) {
                        etape.queryParam("unite", unite);
                    }
                    return etape.build(walletCode);
                })
                .retrieve()
                .body(LISTE_FACTURES);
    }
}
