package com.payment.web;

import com.payment.service.FacturePaymentService;
import com.payment.service.FactureQueryService;
import com.payment.web.dto.FactureDto;
import com.payment.web.dto.PayRequest;
import com.payment.web.dto.PayResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/pay")
    public PayResult payer(@RequestBody PayRequest request) {
        return paymentService.payer(request.walletCode(), request.references());
    }
}
