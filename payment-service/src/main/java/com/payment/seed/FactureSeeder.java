package com.payment.seed;

import com.payment.domain.Facture;
import com.payment.facturation.FacturationStrategy;
import com.payment.facturation.FacturationStrategyFactory;
import com.payment.repository.FactureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class FactureSeeder {

    private final FactureRepository factureRepository;
    private final FacturationStrategyFactory strategyFactory;
    private final Clock clock;

    public FactureSeeder(FactureRepository factureRepository,
                         FacturationStrategyFactory strategyFactory,
                         Clock clock) {
        this.factureRepository = factureRepository;
        this.strategyFactory = strategyFactory;
        this.clock = clock;
    }

    @Transactional
    public int seed(int numWallets) {
        factureRepository.deleteAllInBatch();

        YearMonth moisCourant = YearMonth.now(clock);
        List<Facture> factures = new ArrayList<>();
        for (int index = 1; index <= numWallets; index++) {
            for (FacturationStrategy strategy : strategyFactory.all()) {
                factures.addAll(strategy.genererFactures(index, moisCourant));
            }
        }

        factureRepository.saveAll(factures);
        return factures.size();
    }
}
