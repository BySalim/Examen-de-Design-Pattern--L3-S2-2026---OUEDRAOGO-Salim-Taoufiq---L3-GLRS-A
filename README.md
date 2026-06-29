# Examen Design Pattern — L3 S2 2026

Deux services REST construits avec Spring Boot.

| Service | Port | Rôle |
|---------|------|------|
| `badwallet-api` | 8080 | Portefeuilles, dépôts, retraits, transferts, paiement de factures |
| `payment-service` | 8081 | Factures ISM et WOYAFAL par portefeuille |

Le `badwallet-api` consomme le `payment-service` (paiement de factures et proxy de consultation).

## Stack

- Java 21
- Spring Boot 3
- Maven
- H2 (base en mémoire, données générées par le seeder au besoin)

## Lancer les services

Chaque service est un projet Maven indépendant.

```bash
# Terminal 1
cd badwallet-api
./mvnw spring-boot:run

# Terminal 2
cd payment-service
./mvnw spring-boot:run
```

## Tester

1. Démarrer les deux services.
2. Générer les données :
   ```
   POST http://localhost:8080/api/wallets/seed?numWallets=10&eventsPerWallet=100
   ```
3. Lancer les requêtes de `test.http` (extension REST Client de VS Code).

## Organisation Git

- `main` : code stable et livrable.
- `develop` : intégration des fonctionnalités terminées et testées.
- `feature/*` : une branche par endpoint, créée depuis `develop` et fusionnée dans `develop`.
