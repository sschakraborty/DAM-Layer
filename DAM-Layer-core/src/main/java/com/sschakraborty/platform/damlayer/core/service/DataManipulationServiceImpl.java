package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.Model;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

import java.util.List;

public class DataManipulationServiceImpl implements DataManipulationService {
    private final TransactionManager transactionManager;

    public DataManipulationServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void insert(String externalText, List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            models.forEach(model -> session.insert(externalText, model));
        });
    }

    @Override
    public void update(String externalText, List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            models.forEach(model -> session.update(externalText, model));
        });
    }

    @Override
    public void save(String externalText, List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            models.forEach(model -> session.save(externalText, model));
        });
    }

    @Override
    public void delete(String externalText, List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            models.forEach(model -> session.delete(externalText, model));
        });
    }
}