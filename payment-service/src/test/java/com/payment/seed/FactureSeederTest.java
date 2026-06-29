package com.payment.seed;

import com.payment.domain.Facture;
import com.payment.domain.ServiceName;
import com.payment.domain.StatutFacture;
import com.payment.repository.FactureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FactureSeederTest {

    @Autowired
    private FactureSeeder seeder;

    @Autowired
    private FactureRepository factureRepository;

    @Test
    void genere_un_jeu_complet() {
        int total = seeder.seed(10);

        assertThat(total).isEqualTo(80);
        assertThat(factureRepository.count()).isEqualTo(80);
    }

    @Test
    void respecte_le_montage_des_factures_du_wallet_3() {
        seeder.seed(10);
        List<Facture> factures = factureRepository.findAll();

        YearMonth mai = YearMonth.of(2026, 5);
        YearMonth juin = YearMonth.of(2026, 6);

        Facture ism1 = trouver(factures, "FAC-ISM-3-1");
        assertThat(ism1.getServiceName()).isEqualTo(ServiceName.ISM);
        assertThat(ism1.getWalletCode()).isEqualTo("WLT-0000003");
        assertThat(ism1.getPeriode()).isEqualTo(mai);
        assertThat(ism1.getStatut()).isEqualTo(StatutFacture.UNPAID);
        assertThat(ism1.getMontant()).isEqualByComparingTo("5000.00");

        Facture ism3 = trouver(factures, "FAC-ISM-3-3");
        assertThat(ism3.getPeriode()).isEqualTo(mai);
        assertThat(ism3.getStatut()).isEqualTo(StatutFacture.UNPAID);

        Facture ism4 = trouver(factures, "FAC-ISM-3-4");
        assertThat(ism4.getPeriode()).isEqualTo(juin);
        assertThat(ism4.getStatut()).isEqualTo(StatutFacture.UNPAID);

        Facture woyafalCourant = trouver(factures, "FAC-WOYAFAL-3-4");
        assertThat(woyafalCourant.getPeriode()).isEqualTo(juin);
        assertThat(woyafalCourant.getStatut()).isEqualTo(StatutFacture.UNPAID);
        assertThat(woyafalCourant.getMontant()).isEqualByComparingTo("10000.00");
    }

    private Facture trouver(List<Facture> factures, String reference) {
        return factures.stream()
                .filter(facture -> facture.getReference().equals(reference))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Facture absente : " + reference));
    }
}
