package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionResult;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.io.Serializable;

public class QueryServiceImpl implements QueryService {
    private static final String FETCH_KEY = "FETCH_OBJ_KEY";

    private final TransactionManager transactionManager;

    public QueryServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T fetch(Class<T> clazz, Serializable id) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            final T fetchedObj = session.get(clazz, id);
            transactionResult.put(FETCH_KEY, fetchedObj);
        });
        return result.hasKey(FETCH_KEY) ? (T) result.get(FETCH_KEY) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T fetchTree(Class<T> clazz, Serializable id) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            final T fetchedObj = session.get(clazz, id);
            {
                Hibernate.unproxy(fetchedObj);
            }
            transactionResult.put(FETCH_KEY, fetchedObj);
        });
        return result.hasKey(FETCH_KEY) ? (T) result.get(FETCH_KEY) : null;
    }
}