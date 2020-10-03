package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.processor.CallbackHandler;
import com.sschakraborty.platform.damlayer.core.processor.CallbackHandlerManager;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SessionWrapperImpl implements SessionWrapper {
    private final Session session;
    private final CallbackHandlerManager callbackHandlerManager;
    private final TenantConfiguration tenantConfiguration;

    public SessionWrapperImpl(Session session, CallbackHandlerManager callbackHandlerManager, TenantConfiguration tenantConfiguration) {
        this.session = session;
        this.callbackHandlerManager = callbackHandlerManager;
        this.tenantConfiguration = tenantConfiguration;
    }

    @Override
    public void insert(String externalText, Object model) {
        final LinkedList<CallbackHandler> handlersForModel = callbackHandlerManager.getHandlerFor(model.getClass());
        boolean success = false;
        try {
            callbackHandlerManager.getDefaultHandler().preInsert(model, externalText, tenantConfiguration);
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.preInsert(model, externalText, tenantConfiguration);
            }
            session.save(model);
            success = true;
        } finally {
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.postInsert(model, success, externalText, tenantConfiguration);
            }
            callbackHandlerManager.getDefaultHandler().postInsert(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void update(String externalText, Object model) {
        final LinkedList<CallbackHandler> handlersForModel = callbackHandlerManager.getHandlerFor(model.getClass());
        boolean success = false;
        try {
            callbackHandlerManager.getDefaultHandler().preUpdate(model, externalText, tenantConfiguration);
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.preUpdate(model, externalText, tenantConfiguration);
            }
            session.update(model);
            success = true;
        } finally {
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.postUpdate(model, success, externalText, tenantConfiguration);
            }
            callbackHandlerManager.getDefaultHandler().postUpdate(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void save(String externalText, Object model) {
        final LinkedList<CallbackHandler> handlersForModel = callbackHandlerManager.getHandlerFor(model.getClass());
        boolean success = false;
        try {
            callbackHandlerManager.getDefaultHandler().preSave(model, externalText, tenantConfiguration);
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.preSave(model, externalText, tenantConfiguration);
            }
            session.saveOrUpdate(model);
            success = true;
        } finally {
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.postSave(model, success, externalText, tenantConfiguration);
            }
            callbackHandlerManager.getDefaultHandler().postSave(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void delete(String externalText, Object model) {
        final LinkedList<CallbackHandler> handlersForModel = callbackHandlerManager.getHandlerFor(model.getClass());
        boolean success = false;
        try {
            callbackHandlerManager.getDefaultHandler().preDelete(model, externalText, tenantConfiguration);
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.preDelete(model, externalText, tenantConfiguration);
            }
            session.delete(model);
            success = true;
        } finally {
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.postDelete(model, success, externalText, tenantConfiguration);
            }
            callbackHandlerManager.getDefaultHandler().postDelete(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fetch(String externalText, Class<T> clazz, Serializable id) {
        final LinkedList<CallbackHandler> handlersForModel = callbackHandlerManager.getHandlerFor(clazz);
        T fetchedObject = null;
        try {
            callbackHandlerManager.getDefaultHandler().preFetch((Class<Object>) clazz, id, externalText, tenantConfiguration);
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.preFetch((Class<Object>) clazz, id, externalText, tenantConfiguration);
            }
            fetchedObject = session.get(clazz, id);
        } finally {
            for (final CallbackHandler callbackHandler : handlersForModel) {
                callbackHandler.postFetch((Class<Object>) clazz, id, fetchedObject, externalText, tenantConfiguration);
            }
            callbackHandlerManager.getDefaultHandler().postFetch((Class<Object>) clazz, id, fetchedObject, externalText, tenantConfiguration);
        }
        return fetchedObject;
    }

    @Override
    public <S> CriteriaQuery<S> createCriteriaQuery(Class<S> clazz) {
        return session.getCriteriaBuilder().createQuery(clazz);
    }

    @Override
    public <S> List<S> executeSelect(CriteriaQuery<S> criteriaQuery, int offset, int limit) {
        return executeTypedSelectI(offset, limit, session.createQuery(criteriaQuery));
    }

    @Override
    public <S> int executeUpdate(CriteriaQuery<S> criteriaQuery) {
        return session.createQuery(criteriaQuery).executeUpdate();
    }

    @Override
    public <S> List<S> executeSelect(Class<S> clazz, String jpql, int offset, int limit) {
        return executeTypedSelectI(offset, limit, session.createQuery(jpql, clazz));
    }

    private <S> List<S> executeTypedSelectI(int offset, int limit, TypedQuery<S> query) {
        query.setFirstResult(Math.max(0, offset));
        query.setMaxResults(Math.max(0, limit));
        return query.getResultList();
    }
}