package com.payment.service;

import com.payment.domain.Facture;
import com.payment.domain.ServiceName;
import com.payment.domain.StatutFacture;
import com.payment.repository.FactureRepository;
import com.payment.web.dto.FactureDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.YearMonth;
import java.util.List;

@Service
public class FactureQueryService {

    private final FactureRepository factureRepository;
    private final Clock clock;

    public FactureQueryService(FactureRepository factureRepository, Clock clock) {
        this.factureRepository = factureRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<FactureDto> courantesImpayees(String walletCode, String unite) {
        YearMonth moisCourant = YearMonth.now(clock);
        List<Facture> factures = (unite == null || unite.isBlank())
                ? factureRepository.findByWalletCodeAndPeriodeAndStatut(walletCode, moisCourant, StatutFacture.UNPAID)
                : factureRepository.findByWalletCodeAndPeriodeAndStatutAndServiceName(
                        walletCode, moisCourant, StatutFacture.UNPAID, parseUnite(unite));
        return factures.stream().map(FactureDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<FactureDto> parReferences(String walletCode, List<String> references) {
        return factureRepository.findByWalletCodeAndReferenceIn(walletCode, references).stream()
                .map(FactureDto::from)
                .toList();
    }

    private ServiceName parseUnite(String unite) {
        try {
            return ServiceName.valueOf(unite);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unite de paiement invalide : " + unite);
        }
    }
}
