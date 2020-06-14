package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import org.hibernate.Session;

import java.util.List;

public class DataManipulationServiceImpl implements DataManipulationService {
    private final TransactionManager transactionManager;

    public DataManipulationServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void insert(List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            models.forEach(session::save);
        });
    }

    @Override
    public void update(List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            models.forEach(session::update);
        });
    }

    @Override
    public void save(List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            models.forEach(session::saveOrUpdate);
        });
    }

    @Override
    public void delete(List<Model> models) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            models.forEach(session::delete);
        });
    }
}