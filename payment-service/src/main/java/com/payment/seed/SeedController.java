package com.payment.seed;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class SeedController {

    private final FactureSeeder seeder;

    public SeedController(FactureSeeder seeder) {
        this.seeder = seeder;
    }

    @PostMapping("/seed")
    public Map<String, Object> seed(@RequestParam(defaultValue = "10") int numWallets) {
        int facturesCreees = seeder.seed(numWallets);
        return Map.of(
                "numWallets", numWallets,
                "facturesCreees", facturesCreees
        );
    }
}
