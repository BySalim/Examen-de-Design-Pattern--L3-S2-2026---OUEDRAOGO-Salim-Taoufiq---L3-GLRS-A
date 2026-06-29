package com.badwallet.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedRunner implements CommandLineRunner {

    private static final int NOMBRE_WALLETS_PAR_DEFAUT = 10;
    private static final int EVENEMENTS_PAR_DEFAUT = 100;

    private final WalletSeeder walletSeeder;
    private final PaymentSeedClient paymentSeedClient;

    public SeedRunner(WalletSeeder walletSeeder, PaymentSeedClient paymentSeedClient) {
        this.walletSeeder = walletSeeder;
        this.paymentSeedClient = paymentSeedClient;
    }

    @Override
    public void run(String... args) {
        walletSeeder.seed(NOMBRE_WALLETS_PAR_DEFAUT, EVENEMENTS_PAR_DEFAUT);
        paymentSeedClient.declencherSeed(NOMBRE_WALLETS_PAR_DEFAUT);
    }
}
