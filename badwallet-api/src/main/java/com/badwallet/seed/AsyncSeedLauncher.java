package com.badwallet.seed;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncSeedLauncher {

    private final WalletSeeder walletSeeder;
    private final PaymentSeedClient paymentSeedClient;

    public AsyncSeedLauncher(WalletSeeder walletSeeder, PaymentSeedClient paymentSeedClient) {
        this.walletSeeder = walletSeeder;
        this.paymentSeedClient = paymentSeedClient;
    }

    @Async
    public void lancer(int numWallets, int eventsPerWallet) {
        walletSeeder.seed(numWallets, eventsPerWallet);
        paymentSeedClient.declencherSeed(numWallets);
    }
}
