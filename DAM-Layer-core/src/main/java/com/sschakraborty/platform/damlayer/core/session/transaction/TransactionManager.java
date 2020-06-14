package com.sschakraborty.platform.damlayer.core.session.transaction;

public interface TransactionManager {
    TransactionResult executeStateful(TransactionJob transactionJob);
}