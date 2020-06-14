package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;

public class QueryServiceImpl implements QueryService {
    private final TransactionManager transactionManager;

    public QueryServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}