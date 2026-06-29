package com.badwallet.transaction.event;

import com.badwallet.domain.Transaction;

public record TransactionEvent(Transaction transaction) {
}
