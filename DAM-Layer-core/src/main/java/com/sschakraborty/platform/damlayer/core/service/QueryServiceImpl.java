package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionResult;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.core.util.ProxyUtil;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.io.Serializable;

public class QueryServiceImpl implements QueryService {
    private static final String FETCH_KEY = "FETCH_OBJ_KEY";

    private final TransactionManager transactionManager;

    public QueryServiceImpl(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T fetch(String externalText, Class<T> clazz, Serializable id) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            final T fetchedObj = session.fetch(externalText, clazz, id);
            transactionResult.put(FETCH_KEY, fetchedObj);
        });
        return result.hasKey(FETCH_KEY) ? (T) result.get(FETCH_KEY) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> T fetchTree(String externalText, Class<T> clazz, Serializable id) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            final T fetchedObj = ProxyUtil.traverseProxyTree(session.fetch(externalText, clazz, id));
            transactionResult.put(FETCH_KEY, fetchedObj);
        });
        return result.hasKey(FETCH_KEY) ? (T) result.get(FETCH_KEY) : null;
    }
}