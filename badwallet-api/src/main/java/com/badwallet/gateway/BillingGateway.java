package com.badwallet.gateway;

import java.util.List;

public interface BillingGateway {

    List<RemoteFacture> facturesCourantes(String walletCode, String serviceName);

    List<RemoteFacture> facturesParReferences(String walletCode, List<String> references);

    void marquerPayees(String walletCode, List<String> references);
}
