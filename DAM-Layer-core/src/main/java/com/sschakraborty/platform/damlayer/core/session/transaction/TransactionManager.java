package com.sschakraborty.platform.damlayer.core.session.transaction;

import com.sschakraborty.platform.damlayer.core.session.IsolationMode;

public interface TransactionManager {
    default TransactionResult executeStateful(TransactionJob transactionJob) {
        return this.executeStateful(IsolationMode.TRANSACTION_READ_COMMITTED, transactionJob);
    }

    TransactionResult executeStateful(IsolationMode isolationMode, TransactionJob transactionJob);
}