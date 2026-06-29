package com.badwallet.seed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class SeedController {

    private final AsyncSeedLauncher launcher;

    public SeedController(AsyncSeedLauncher launcher) {
        this.launcher = launcher;
    }

    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> seed(
            @RequestParam(defaultValue = "10") int numWallets,
            @RequestParam(defaultValue = "100") int eventsPerWallet) {
        launcher.lancer(numWallets, eventsPerWallet);
        return ResponseEntity.accepted().body(Map.of(
                "status", "SEEDING_STARTED",
                "numWallets", numWallets,
                "eventsPerWallet", eventsPerWallet
        ));
    }
}
