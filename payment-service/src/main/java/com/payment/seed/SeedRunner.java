package com.payment.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedRunner implements CommandLineRunner {

    private static final int NOMBRE_WALLETS_PAR_DEFAUT = 10;

    private final FactureSeeder seeder;

    public SeedRunner(FactureSeeder seeder) {
        this.seeder = seeder;
    }

    @Override
    public void run(String... args) {
        seeder.seed(NOMBRE_WALLETS_PAR_DEFAUT);
    }
}
