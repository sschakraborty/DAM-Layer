package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;

public class DataServiceImpl implements DataService {
    private final TransactionManager transactionManager;
    private final DataManipulationService dataManipulationService;
    private final QueryService queryService;

    public DataServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.dataManipulationService = new DataManipulationServiceImpl(transactionManager);
        this.queryService = new QueryServiceImpl(transactionManager);
    }

    @Override
    public DataManipulationService getDataManipulationService() {
        return this.dataManipulationService;
    }

    @Override
    public QueryService getQueryService() {
        return this.queryService;
    }

    @Override
    public TransactionManager transactionManager() {
        return this.transactionManager;
    }
}