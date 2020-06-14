package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;

public interface DataService {
    DataManipulationService getDataManipulationService();

    QueryService getQueryService();

    TransactionManager transactionManager();
}