package com.payment.web;

import com.payment.service.FacturePaymentService;
import com.payment.service.FactureQueryService;
import com.payment.web.dto.FactureDto;
import com.payment.web.dto.PayRequest;
import com.payment.web.dto.PayResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/internal/factures")
public class FactureController {

    private final FactureQueryService queryService;
    private final FacturePaymentService paymentService;

    public FactureController(FactureQueryService queryService, FacturePaymentService paymentService) {
        this.queryService = queryService;
        this.paymentService = paymentService;
    }

    @GetMapping("/{walletCode}/current")
    public List<FactureDto> courantes(@PathVariable String walletCode,
                                      @RequestParam(required = false) String unite) {
        return queryService.courantesImpayees(walletCode, unite);
    }

    @GetMapping("/{walletCode}/by-references")
    public List<FactureDto> parReferences(@PathVariable String walletCode,
                                          @RequestParam List<String> references) {
        return queryService.parReferences(walletCode, references);
    }

    @GetMapping("/{walletCode}/periode")
    public List<FactureDto> parPeriode(
            @PathVariable String walletCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(required = false) String unite) {
        return queryService.impayeesSurPeriode(walletCode, debut, fin, unite);
    }

    @PostMapping("/pay")
    public PayResult payer(@RequestBody PayRequest request) {
        return paymentService.payer(request.walletCode(), request.references());
    }
}
