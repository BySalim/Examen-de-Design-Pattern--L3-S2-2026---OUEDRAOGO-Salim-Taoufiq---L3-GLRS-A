package com.badwallet.web;

import com.badwallet.gateway.RemoteFacture;
import com.badwallet.proxy.FactureQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/external/factures")
public class ExternalFactureController {

    private final FactureQueryService factureQueryService;

    public ExternalFactureController(FactureQueryService factureQueryService) {
        this.factureQueryService = factureQueryService;
    }

    @GetMapping("/{walletCode}/current")
    public List<RemoteFacture> courantes(@PathVariable String walletCode,
                                         @RequestParam(required = false) String unite) {
        return factureQueryService.courantes(walletCode, unite);
    }

    @GetMapping("/{walletCode}/periode")
    public List<RemoteFacture> periode(@PathVariable String walletCode,
                                       @RequestParam String debut,
                                       @RequestParam String fin,
                                       @RequestParam(required = false) String unite) {
        return factureQueryService.impayeesSurPeriode(walletCode, debut, fin, unite);
    }
}
