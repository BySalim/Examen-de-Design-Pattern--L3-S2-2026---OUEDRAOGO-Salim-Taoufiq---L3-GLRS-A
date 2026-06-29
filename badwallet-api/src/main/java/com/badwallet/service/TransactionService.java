package com.badwallet.service;

import com.badwallet.domain.PaymentMethod;
import com.badwallet.domain.Transaction;
import com.badwallet.domain.TransactionDirection;
import com.badwallet.domain.TransactionType;
import com.badwallet.domain.Wallet;
import com.badwallet.error.InsufficientBalanceException;
import com.badwallet.error.WalletNotFoundException;
import com.badwallet.repository.WalletRepository;
import com.badwallet.transaction.FeePolicy;
import com.badwallet.transaction.deposit.DepositStrategy;
import com.badwallet.transaction.deposit.DepositStrategyFactory;
import com.badwallet.transaction.event.TransactionEvent;
import com.badwallet.web.dto.DepositRequest;
import com.badwallet.web.dto.TransactionResponse;
import com.badwallet.web.dto.TransferRequest;
import com.badwallet.web.dto.TransferResponse;
import com.badwallet.web.dto.WithdrawRequest;
import com.badwallet.web.dto.WithdrawResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final DepositStrategyFactory depositStrategyFactory;
    private final FeePolicy feePolicy;
    private final ApplicationEventPublisher publisher;
    private final Clock clock;

    public TransactionService(WalletRepository walletRepository,
                              DepositStrategyFactory depositStrategyFactory,
                              FeePolicy feePolicy,
                              ApplicationEventPublisher publisher,
                              Clock clock) {
        this.walletRepository = walletRepository;
        this.depositStrategyFactory = depositStrategyFactory;
        this.feePolicy = feePolicy;
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

    @Transactional
    public WithdrawResponse retirer(WithdrawRequest request) {
        Wallet wallet = walletRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Portefeuille introuvable pour le telephone : " + request.phoneNumber()));

        BigDecimal fee = feePolicy.fraisRetrait(request.amount());
        BigDecimal totalDebited = request.amount().add(fee);
        if (wallet.getBalance().compareTo(totalDebited) < 0) {
            throw new InsufficientBalanceException(
                    "Solde insuffisant pour retirer " + totalDebited + " " + wallet.getCurrency());
        }

        wallet.debiter(totalDebited);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.WITHDRAW)
                .direction(TransactionDirection.DEBIT)
                .amount(request.amount())
                .fee(fee)
                .balanceAfter(wallet.getBalance())
                .currency(wallet.getCurrency())
                .description("Retrait")
                .createdAt(Instant.now(clock))
                .build();
        publisher.publishEvent(new TransactionEvent(transaction));

        return new WithdrawResponse(
                wallet.getPhoneNumber(),
                request.amount(),
                fee,
                totalDebited,
                wallet.getBalance(),
                wallet.getCurrency());
    }

    @Transactional
    public TransferResponse transferer(TransferRequest request) {
        if (request.senderPhone().equals(request.receiverPhone())) {
            throw new IllegalArgumentException("L'emetteur et le destinataire doivent etre differents");
        }

        Wallet sender = walletRepository.findByPhoneNumber(request.senderPhone())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Portefeuille emetteur introuvable : " + request.senderPhone()));
        Wallet receiver = walletRepository.findByPhoneNumber(request.receiverPhone())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Portefeuille destinataire introuvable : " + request.receiverPhone()));

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(
                    "Solde insuffisant pour transferer " + request.amount() + " " + sender.getCurrency());
        }

        sender.debiter(request.amount());
        receiver.crediter(request.amount());
        Instant maintenant = Instant.now(clock);

        Transaction debit = Transaction.builder()
                .wallet(sender)
                .type(TransactionType.TRANSFER)
                .direction(TransactionDirection.DEBIT)
                .amount(request.amount())
                .balanceAfter(sender.getBalance())
                .currency(sender.getCurrency())
                .counterpartyPhone(receiver.getPhoneNumber())
                .description("Transfert emis")
                .createdAt(maintenant)
                .build();
        publisher.publishEvent(new TransactionEvent(debit));

        Transaction credit = Transaction.builder()
                .wallet(receiver)
                .type(TransactionType.TRANSFER)
                .direction(TransactionDirection.CREDIT)
                .amount(request.amount())
                .balanceAfter(receiver.getBalance())
                .currency(receiver.getCurrency())
                .counterpartyPhone(sender.getPhoneNumber())
                .description("Transfert recu")
                .createdAt(maintenant)
                .build();
        publisher.publishEvent(new TransactionEvent(credit));

        return new TransferResponse(
                sender.getPhoneNumber(),
                receiver.getPhoneNumber(),
                request.amount(),
                sender.getBalance(),
                receiver.getBalance(),
                sender.getCurrency());
    }

    private PaymentMethod parseMethod(String value) {
        try {
            return PaymentMethod.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Methode de paiement invalide : " + value);
        }
    }
}
