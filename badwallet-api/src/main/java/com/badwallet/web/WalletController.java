package com.badwallet.web;

import com.badwallet.domain.Wallet;
import com.badwallet.service.TransactionService;
import com.badwallet.service.WalletService;
import com.badwallet.web.dto.BalanceResponse;
import com.badwallet.web.dto.CreateWalletRequest;
import com.badwallet.web.dto.DepositRequest;
import com.badwallet.web.dto.TransactionResponse;
import com.badwallet.web.dto.TransferRequest;
import com.badwallet.web.dto.TransferResponse;
import com.badwallet.web.dto.WalletResponse;
import com.badwallet.web.dto.WithdrawRequest;
import com.badwallet.web.dto.WithdrawResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> creer(@Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.creer(request);
        return ResponseEntity
                .created(URI.create("/api/wallets/" + wallet.getId()))
                .body(WalletResponse.from(wallet));
    }

    @GetMapping
    public Page<WalletResponse> lister(Pageable pageable) {
        return walletService.lister(pageable).map(WalletResponse::from);
    }

    @GetMapping("/{phone}")
    public WalletResponse consulter(@PathVariable String phone) {
        return WalletResponse.from(walletService.consulterParTelephone(phone));
    }

    @GetMapping("/{phone}/balance")
    public BalanceResponse solde(@PathVariable String phone) {
        return BalanceResponse.from(walletService.consulterParTelephone(phone));
    }

    @PostMapping("/{id}/deposit")
    public TransactionResponse deposer(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        return transactionService.deposer(id, request);
    }

    @PostMapping("/withdraw")
    public WithdrawResponse retirer(@Valid @RequestBody WithdrawRequest request) {
        return transactionService.retirer(request);
    }

    @PostMapping("/transfer")
    public TransferResponse transferer(@Valid @RequestBody TransferRequest request) {
        return transactionService.transferer(request);
    }
}
