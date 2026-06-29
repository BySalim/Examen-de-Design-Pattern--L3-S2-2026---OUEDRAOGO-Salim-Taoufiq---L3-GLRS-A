package com.badwallet.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "wallets",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wallet_phone", columnNames = "phone_number"),
                @UniqueConstraint(name = "uk_wallet_code", columnNames = "code")
        }
)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 3)
    private String currency;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Version
    private Long version;

    protected Wallet() {
    }

    private Wallet(Builder builder) {
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.balance = builder.balance;
        this.code = builder.code;
        this.currency = builder.currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void crediter(BigDecimal montant) {
        this.balance = this.balance.add(montant);
    }

    public void debiter(BigDecimal montant) {
        this.balance = this.balance.subtract(montant);
    }

    public void ajouterTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCode() {
        return code;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getVersion() {
        return version;
    }

    public static class Builder {

        private String phoneNumber;
        private String email;
        private BigDecimal balance = BigDecimal.ZERO;
        private String code;
        private String currency = "XOF";

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Wallet build() {
            if (phoneNumber == null || email == null || code == null) {
                throw new IllegalStateException("Wallet incomplet : phoneNumber, email et code sont requis");
            }
            return new Wallet(this);
        }
    }
}
