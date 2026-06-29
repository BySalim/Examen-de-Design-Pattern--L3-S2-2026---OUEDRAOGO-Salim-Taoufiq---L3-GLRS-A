package com.badwallet.transaction.event;

import com.badwallet.repository.TransactionRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionHistoryListener {

    private final TransactionRepository transactionRepository;

    public TransactionHistoryListener(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @EventListener
    public void enregistrer(TransactionEvent event) {
        transactionRepository.save(event.transaction());
    }
}
