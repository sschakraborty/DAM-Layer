package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;

public class DataManipulationServiceImpl implements DataManipulationService {
    private final TransactionManager transactionManager;

    public DataManipulationServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}