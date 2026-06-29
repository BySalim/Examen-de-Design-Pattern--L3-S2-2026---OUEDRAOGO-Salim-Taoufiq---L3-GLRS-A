package com.badwallet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "transactions",
        indexes = @Index(name = "idx_transaction_wallet", columnList = "wallet_id, created_at")
)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private TransactionDirection direction;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal fee;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "counterparty_phone", length = 20)
    private String counterpartyPhone;

    @Column(length = 40)
    private String reference;

    @Column(length = 120)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Transaction() {
    }

    private Transaction(Builder builder) {
        this.wallet = builder.wallet;
        this.type = builder.type;
        this.direction = builder.direction;
        this.amount = builder.amount;
        this.fee = builder.fee;
        this.balanceAfter = builder.balanceAfter;
        this.currency = builder.currency;
        this.counterpartyPhone = builder.counterpartyPhone;
        this.reference = builder.reference;
        this.description = builder.description;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCounterpartyPhone() {
        return counterpartyPhone;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static class Builder {

        private Wallet wallet;
        private TransactionType type;
        private TransactionDirection direction;
        private BigDecimal amount;
        private BigDecimal fee = BigDecimal.ZERO;
        private BigDecimal balanceAfter;
        private String currency = "XOF";
        private String counterpartyPhone;
        private String reference;
        private String description;
        private Instant createdAt;

        public Builder wallet(Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder direction(TransactionDirection direction) {
            this.direction = direction;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder fee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public Builder balanceAfter(BigDecimal balanceAfter) {
            this.balanceAfter = balanceAfter;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder counterpartyPhone(String counterpartyPhone) {
            this.counterpartyPhone = counterpartyPhone;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Transaction build() {
            if (wallet == null || type == null || direction == null
                    || amount == null || balanceAfter == null || createdAt == null) {
                throw new IllegalStateException(
                        "Transaction incomplete : wallet, type, direction, amount, balanceAfter et createdAt sont requis");
            }
            return new Transaction(this);
        }
    }
}
