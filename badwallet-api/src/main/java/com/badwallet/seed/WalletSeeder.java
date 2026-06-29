package com.badwallet.seed;

import com.badwallet.domain.Transaction;
import com.badwallet.domain.TransactionDirection;
import com.badwallet.domain.TransactionType;
import com.badwallet.domain.Wallet;
import com.badwallet.repository.WalletRepository;
import com.badwallet.support.DeterministicReferences;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WalletSeeder {

    private static final BigDecimal SOLDE_INITIAL = new BigDecimal("100000.00");
    private static final BigDecimal SOLDE_PLANCHER = new BigDecimal("50000.00");
    private static final BigDecimal PAS_MONTANT = new BigDecimal("500");
    private static final BigDecimal TAUX_FRAIS = new BigDecimal("0.01");
    private static final BigDecimal PLAFOND_FRAIS = new BigDecimal("5000");

    private final WalletRepository walletRepository;
    private final DeterministicReferences references;
    private final Clock clock;

    public WalletSeeder(WalletRepository walletRepository,
                        DeterministicReferences references,
                        Clock clock) {
        this.walletRepository = walletRepository;
        this.references = references;
        this.clock = clock;
    }

    @Transactional
    public int seed(int numWallets, int eventsPerWallet) {
        if (walletRepository.count() > 0) {
            return 0;
        }

        List<Wallet> wallets = new ArrayList<>();
        for (int index = 1; index <= numWallets; index++) {
            Wallet wallet = Wallet.builder()
                    .phoneNumber(references.phone(index))
                    .code(references.walletCode(index))
                    .email("wallet" + index + "@badwallet.sn")
                    .currency("XOF")
                    .balance(SOLDE_INITIAL)
                    .build();
            genererHistorique(wallet, index, eventsPerWallet, numWallets);
            wallets.add(wallet);
        }

        walletRepository.saveAll(wallets);
        return wallets.size();
    }

    private void genererHistorique(Wallet wallet, int index, int eventsPerWallet, int numWallets) {
        Random aleatoire = new Random(index);
        Instant reference = Instant.now(clock);

        for (int rang = 1; rang <= eventsPerWallet; rang++) {
            BigDecimal montant = PAS_MONTANT
                    .multiply(BigDecimal.valueOf(1 + aleatoire.nextInt(40)))
                    .setScale(2, RoundingMode.HALF_UP);

            TransactionType type = tirerType(aleatoire);
            BigDecimal frais = type == TransactionType.WITHDRAW ? calculerFrais(montant) : zero();
            boolean debit = type != TransactionType.DEPOSIT;
            BigDecimal mouvement = debit ? montant.add(frais) : montant;

            if (debit && wallet.getBalance().subtract(mouvement).compareTo(SOLDE_PLANCHER) < 0) {
                type = TransactionType.DEPOSIT;
                debit = false;
                frais = zero();
                mouvement = montant;
            }

            if (debit) {
                wallet.debiter(mouvement);
            } else {
                wallet.crediter(mouvement);
            }

            Transaction transaction = Transaction.builder()
                    .wallet(wallet)
                    .type(type)
                    .direction(debit ? TransactionDirection.DEBIT : TransactionDirection.CREDIT)
                    .amount(montant)
                    .fee(frais)
                    .balanceAfter(wallet.getBalance())
                    .currency("XOF")
                    .counterpartyPhone(type == TransactionType.TRANSFER
                            ? references.phone(autreIndex(index, numWallets, aleatoire)) : null)
                    .reference(type == TransactionType.PAYMENT
                            ? referenceFacture(index, aleatoire) : null)
                    .description(libelle(type))
                    .createdAt(reference.minus(Duration.ofDays(eventsPerWallet - rang)))
                    .build();

            wallet.ajouterTransaction(transaction);
        }
    }

    private TransactionType tirerType(Random aleatoire) {
        int tirage = aleatoire.nextInt(100);
        if (tirage < 50) {
            return TransactionType.DEPOSIT;
        }
        if (tirage < 75) {
            return TransactionType.WITHDRAW;
        }
        if (tirage < 90) {
            return TransactionType.TRANSFER;
        }
        return TransactionType.PAYMENT;
    }

    private BigDecimal calculerFrais(BigDecimal montant) {
        return montant.multiply(TAUX_FRAIS).min(PLAFOND_FRAIS).setScale(2, RoundingMode.HALF_UP);
    }

    private int autreIndex(int index, int numWallets, Random aleatoire) {
        if (numWallets <= 1) {
            return index;
        }
        int autre;
        do {
            autre = 1 + aleatoire.nextInt(numWallets);
        } while (autre == index);
        return autre;
    }

    private String referenceFacture(int index, Random aleatoire) {
        String service = aleatoire.nextBoolean() ? "ISM" : "WOYAFAL";
        return "FAC-%s-%d-%d".formatted(service, index, 1 + aleatoire.nextInt(4));
    }

    private String libelle(TransactionType type) {
        return switch (type) {
            case DEPOSIT -> "Depot";
            case WITHDRAW -> "Retrait";
            case TRANSFER -> "Transfert";
            case PAYMENT -> "Paiement facture";
        };
    }

    private BigDecimal zero() {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
