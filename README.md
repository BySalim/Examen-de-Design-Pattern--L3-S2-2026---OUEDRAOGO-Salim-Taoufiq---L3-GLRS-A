# BadWallet API & Payment Service

Deux services REST développés avec Spring Boot.

| Service | Port | Rôle |
|---------|------|------|
| `badwallet-api` | 8080 | Portefeuilles : création, dépôt, retrait, transfert, paiement de factures, historique |
| `payment-service` | 8081 | Factures ISM et WOYAFAL par portefeuille (service interne) |

Le `badwallet-api` s'appuie sur le `payment-service` pour le paiement et la consultation des factures.

## Pré-requis

- Java 21
- Base H2 en mémoire : aucune installation de base de données n'est requise.

## Lancer

Les deux modules sont des projets Maven indépendants.

```bash
# Terminal 1
cd payment-service
./mvnw spring-boot:run

# Terminal 2
cd badwallet-api
./mvnw spring-boot:run
```

## Tester

Les données (portefeuilles et factures) sont générées automatiquement au démarrage.
Le fichier `test.http` regroupe l'ensemble des requêtes et s'utilise avec l'extension REST Client de VS Code.
Un redémarrage des services repart d'un état neuf, la base étant recréée à chaque lancement.

## Patterns mis en œuvre

Strategy, Factory, Proxy, Adapter, Repository, Observer, Template Method, Builder, Singleton.

## Organisation Git

- `main` : code stable et livrable.
- `develop` : intégration des fonctionnalités terminées et testées.
- `feature/*` : une branche par endpoint, créée depuis `develop` puis fusionnée dans `develop`.
