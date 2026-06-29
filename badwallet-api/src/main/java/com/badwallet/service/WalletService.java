package com.badwallet.service;

import com.badwallet.domain.Wallet;
import com.badwallet.error.DuplicateResourceException;
import com.badwallet.error.WalletNotFoundException;
import com.badwallet.repository.WalletRepository;
import com.badwallet.web.dto.CreateWalletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public Page<Wallet> lister(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Wallet consulterParTelephone(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Portefeuille introuvable pour le telephone : " + phoneNumber));
    }

    @Transactional
    public Wallet creer(CreateWalletRequest request) {
        if (walletRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException("Numero de telephone deja utilise : " + request.phoneNumber());
        }
        if (walletRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Code de portefeuille deja utilise : " + request.code());
        }

        Wallet wallet = Wallet.builder()
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .code(request.code())
                .currency(request.currency())
                .balance(request.initialBalance())
                .build();

        return walletRepository.save(wallet);
    }
}
