package com.payment.repository;

import com.payment.domain.Facture;
import com.payment.domain.ServiceName;
import com.payment.domain.StatutFacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findByWalletCodeAndPeriodeAndStatut(String walletCode, YearMonth periode, StatutFacture statut);

    List<Facture> findByWalletCodeAndPeriodeAndStatutAndServiceName(
            String walletCode, YearMonth periode, StatutFacture statut, ServiceName serviceName);

    List<Facture> findByWalletCodeAndReferenceIn(String walletCode, List<String> references);

    List<Facture> findByWalletCodeAndStatutAndDateEmissionGreaterThanEqualAndDateEmissionLessThanOrderByDateEmissionAsc(
            String walletCode, StatutFacture statut, LocalDate debut, LocalDate fin);

    List<Facture> findByWalletCodeAndServiceNameAndStatutAndDateEmissionGreaterThanEqualAndDateEmissionLessThanOrderByDateEmissionAsc(
            String walletCode, ServiceName serviceName, StatutFacture statut, LocalDate debut, LocalDate fin);
}
