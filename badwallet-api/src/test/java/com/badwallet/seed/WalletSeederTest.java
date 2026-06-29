package com.badwallet.seed;

import com.badwallet.domain.Wallet;
import com.badwallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WalletSeederTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void le_boot_seede_dix_wallets_deterministes() {
        List<Wallet> wallets = walletRepository.findAll(Sort.by("id"));

        assertThat(wallets).hasSize(10);

        Wallet troisieme = wallets.get(2);
        assertThat(troisieme.getId()).isEqualTo(3L);
        assertThat(troisieme.getPhoneNumber()).isEqualTo("+221770000003");
        assertThat(troisieme.getCode()).isEqualTo("WLT-0000003");

        for (Wallet wallet : wallets) {
            assertThat(wallet.getTransactions()).hasSize(100);
            assertThat(wallet.getBalance()).isGreaterThanOrEqualTo(new BigDecimal("50000.00"));
        }
    }
}
