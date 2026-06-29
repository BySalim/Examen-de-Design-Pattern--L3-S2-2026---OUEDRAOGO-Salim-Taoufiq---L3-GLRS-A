package com.badwallet.service;

import com.badwallet.domain.PaymentMethod;
import com.badwallet.domain.Transaction;
import com.badwallet.domain.TransactionDirection;
import com.badwallet.domain.TransactionType;
import com.badwallet.domain.Wallet;
import com.badwallet.error.WalletNotFoundException;
import com.badwallet.repository.WalletRepository;
import com.badwallet.transaction.deposit.DepositStrategy;
import com.badwallet.transaction.deposit.DepositStrategyFactory;
import com.badwallet.transaction.event.TransactionEvent;
import com.badwallet.web.dto.DepositRequest;
import com.badwallet.web.dto.TransactionResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final DepositStrategyFactory depositStrategyFactory;
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    public TransactionService(WalletRepository walletRepository,
                              DepositStrategyFactory depositStrategyFactory,
                              ApplicationEventPublisher publisher,
                              Clock clock) {
        this.walletRepository = walletRepository;
        this.depositStrategyFactory = depositStrategyFactory;
        this.publisher = publisher;
        this.clock = clock;
    }

    @Transactional
    public TransactionResponse deposer(Long walletId, DepositRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Portefeuille introuvable pour l'id : " + walletId));

        PaymentMethod method = parseMethod(request.paymentMethod());
        DepositStrategy strategy = depositStrategyFactory.resolve(method);
        String description = strategy.appliquer(wallet, request.amount());

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.DEPOSIT)
                .direction(TransactionDirection.CREDIT)
                .amount(request.amount())
                .balanceAfter(wallet.getBalance())
                .currency(wallet.getCurrency())
                .description(description)
                .createdAt(Instant.now(clock))
                .build();
        publisher.publishEvent(new TransactionEvent(transaction));

        return TransactionResponse.from(transaction);
    }

    private PaymentMethod parseMethod(String value) {
        try {
            return PaymentMethod.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Methode de paiement invalide : " + value);
        }
    }
}
