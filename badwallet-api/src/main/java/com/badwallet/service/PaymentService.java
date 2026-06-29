package com.badwallet.service;

import com.badwallet.domain.Transaction;
import com.badwallet.domain.TransactionDirection;
import com.badwallet.domain.TransactionType;
import com.badwallet.domain.Wallet;
import com.badwallet.error.FactureNotFoundException;
import com.badwallet.error.InsufficientBalanceException;
import com.badwallet.error.WalletNotFoundException;
import com.badwallet.gateway.BillingGateway;
import com.badwallet.gateway.RemoteFacture;
import com.badwallet.repository.WalletRepository;
import com.badwallet.transaction.event.TransactionEvent;
import com.badwallet.web.dto.PayFacturesRequest;
import com.badwallet.web.dto.PayRequest;
import com.badwallet.web.dto.PaymentResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class PaymentService {

    private final WalletRepository walletRepository;
    private final BillingGateway billingGateway;
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    public PaymentService(WalletRepository walletRepository,
                          BillingGateway billingGateway,
                          ApplicationEventPublisher publisher,
                          Clock clock) {
        this.walletRepository = walletRepository;
        this.billingGateway = billingGateway;
        this.publisher = publisher;
        this.clock = clock;
    }

    @Transactional
    public PaymentResponse payerMoisCourant(PayRequest request) {
        Wallet wallet = chargerWallet(request.phoneNumber());
        List<RemoteFacture> factures = billingGateway.facturesCourantes(wallet.getCode(), request.serviceName());
        return regler(wallet, factures, request.serviceName());
    }

    @Transactional
    public PaymentResponse payerFactures(PayFacturesRequest request) {
        Wallet wallet = chargerWallet(request.phoneNumber());
        List<RemoteFacture> factures = billingGateway.facturesParReferences(
                wallet.getCode(), request.factureReferences());
        if (factures.size() != request.factureReferences().size()) {
            throw new FactureNotFoundException("Une ou plusieurs factures sont introuvables pour ce portefeuille");
        }
        return regler(wallet, factures, request.serviceName());
    }

    private PaymentResponse regler(Wallet wallet, List<RemoteFacture> factures, String serviceName) {
        List<RemoteFacture> aPayer = factures.stream().filter(RemoteFacture::estImpayee).toList();

        BigDecimal total = aPayer.stream()
                .map(RemoteFacture::montant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (wallet.getBalance().compareTo(total) < 0) {
            throw new InsufficientBalanceException(
                    "Solde insuffisant pour payer " + total + " " + wallet.getCurrency());
        }

        Instant maintenant = Instant.now(clock);
        for (RemoteFacture facture : aPayer) {
            wallet.debiter(facture.montant());
            Transaction transaction = Transaction.builder()
                    .wallet(wallet)
                    .type(TransactionType.PAYMENT)
                    .direction(TransactionDirection.DEBIT)
                    .amount(facture.montant())
                    .balanceAfter(wallet.getBalance())
                    .currency(wallet.getCurrency())
                    .reference(facture.reference())
                    .description("Paiement " + serviceName)
                    .createdAt(maintenant)
                    .build();
            publisher.publishEvent(new TransactionEvent(transaction));
        }

        List<String> references = aPayer.stream().map(RemoteFacture::reference).toList();
        if (!references.isEmpty()) {
            billingGateway.marquerPayees(wallet.getCode(), references);
        }

        return new PaymentResponse(
                wallet.getPhoneNumber(),
                serviceName,
                references,
                total,
                wallet.getBalance(),
                wallet.getCurrency());
    }

    private Wallet chargerWallet(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Portefeuille introuvable pour le telephone : " + phoneNumber));
    }
}
