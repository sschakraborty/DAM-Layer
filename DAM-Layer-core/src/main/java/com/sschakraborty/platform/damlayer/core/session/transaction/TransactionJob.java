package com.sschakraborty.platform.damlayer.core.session.transaction;

public interface TransactionJob {
    void execute(TransactionUnit transactionUnit, TransactionResult transactionResult) throws Exception;
}