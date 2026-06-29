package com.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(
        name = "factures",
        uniqueConstraints = @UniqueConstraint(name = "uk_facture_reference", columnNames = "reference"),
        indexes = {
                @Index(name = "idx_facture_wallet_statut", columnList = "wallet_code, statut"),
                @Index(name = "idx_facture_wallet_periode", columnList = "wallet_code, periode")
        }
)
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String reference;

    @Column(name = "wallet_code", nullable = false, length = 20)
    private String walletCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_name", nullable = false, length = 16)
    private ServiceName serviceName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 3)
    private String devise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private StatutFacture statut;

    @Column(nullable = false, length = 7)
    private YearMonth periode;

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    @Column(length = 120)
    private String libelle;

    @Version
    private Long version;

    protected Facture() {
    }

    private Facture(Builder builder) {
        this.reference = builder.reference;
        this.walletCode = builder.walletCode;
        this.serviceName = builder.serviceName;
        this.montant = builder.montant;
        this.devise = builder.devise;
        this.statut = builder.statut;
        this.periode = builder.periode;
        this.dateEmission = builder.dateEmission;
        this.datePaiement = builder.datePaiement;
        this.libelle = builder.libelle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void payer(LocalDate date) {
        this.statut = StatutFacture.PAID;
        this.datePaiement = date;
    }

    public boolean estPayee() {
        return statut == StatutFacture.PAID;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public String getDevise() {
        return devise;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public YearMonth getPeriode() {
        return periode;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public String getLibelle() {
        return libelle;
    }

    public Long getVersion() {
        return version;
    }

    public static class Builder {

        private String reference;
        private String walletCode;
        private ServiceName serviceName;
        private BigDecimal montant;
        private String devise = "XOF";
        private StatutFacture statut = StatutFacture.UNPAID;
        private YearMonth periode;
        private LocalDate dateEmission;
        private LocalDate datePaiement;
        private String libelle;

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder walletCode(String walletCode) {
            this.walletCode = walletCode;
            return this;
        }

        public Builder serviceName(ServiceName serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder montant(BigDecimal montant) {
            this.montant = montant;
            return this;
        }

        public Builder devise(String devise) {
            this.devise = devise;
            return this;
        }

        public Builder statut(StatutFacture statut) {
            this.statut = statut;
            return this;
        }

        public Builder periode(YearMonth periode) {
            this.periode = periode;
            return this;
        }

        public Builder dateEmission(LocalDate dateEmission) {
            this.dateEmission = dateEmission;
            return this;
        }

        public Builder datePaiement(LocalDate datePaiement) {
            this.datePaiement = datePaiement;
            return this;
        }

        public Builder libelle(String libelle) {
            this.libelle = libelle;
            return this;
        }

        public Facture build() {
            if (reference == null || walletCode == null || serviceName == null
                    || montant == null || periode == null) {
                throw new IllegalStateException(
                        "Facture incomplete : reference, walletCode, serviceName, montant et periode sont requis");
            }
            if (dateEmission == null) {
                dateEmission = periode.atDay(1);
            }
            return new Facture(this);
        }
    }
}
