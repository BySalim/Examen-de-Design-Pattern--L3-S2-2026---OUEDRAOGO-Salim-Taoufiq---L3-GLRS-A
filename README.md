# Examen Design Pattern — L3 S2 2026

Deux services REST Spring Boot.

| Service | Port | Rôle |
|---------|------|------|
| `badwallet-api` | 8080 | Portefeuilles : création, dépôt, retrait, transfert, paiement de factures, historique |
| `payment-service` | 8081 | Factures ISM et WOYAFAL par portefeuille (service interne) |

Le `badwallet-api` appelle le `payment-service` pour payer et consulter les factures. Le professeur ne teste que le port 8080.

## Pré-requis

- Java 21
- Aucune installation de base de données : H2 en mémoire, les données sont générées au démarrage.

## Lancer

Deux projets Maven indépendants, à lancer dans deux terminaux.

```bash
# Terminal 1
cd payment-service
./mvnw spring-boot:run

# Terminal 2
cd badwallet-api
./mvnw spring-boot:run
```

## Tester

Les deux services s'amorcent automatiquement au démarrage (10 portefeuilles et leurs factures).
Ouvrir `test.http` avec l'extension REST Client (VS Code) et lancer les requêtes une à une.

Pour repartir d'un état neuf, redémarrer les services (base recréée à chaque démarrage).

## Organisation Git

- `main` : code stable et livrable.
- `develop` : intégration des fonctionnalités terminées et testées.
- `feature/*` : une branche par endpoint, créée depuis `develop` puis fusionnée dans `develop`.
