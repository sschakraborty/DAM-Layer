package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.audit.core.engine.AuditEngine;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public class SessionWrapperImpl implements SessionWrapper {
    private final Session session;
    private final AuditEngine auditEngine;
    private final TenantConfiguration tenantConfiguration;

    public SessionWrapperImpl(Session session, AuditEngine auditEngine, TenantConfiguration tenantConfiguration) {
        this.session = session;
        this.auditEngine = auditEngine;
        this.tenantConfiguration = tenantConfiguration;
    }

    @Override
    public void insert(String externalText, Model model) {
        boolean success = false;
        try {
            session.save(model);
            success = true;
        } finally {
            auditEngine.generateFor(DataOperation.INSERT, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void update(String externalText, Model model) {
        boolean success = false;
        try {
            session.update(model);
            success = true;
        } finally {
            auditEngine.generateFor(DataOperation.UPDATE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void save(String externalText, Model model) {
        boolean success = false;
        try {
            session.saveOrUpdate(model);
            success = true;
        } finally {
            auditEngine.generateFor(DataOperation.SAVE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void delete(String externalText, Model model) {
        boolean success = false;
        try {
            session.delete(model);
            success = true;
        } finally {
            auditEngine.generateFor(DataOperation.DELETE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public <T extends Model> T fetch(String externalText, Class<T> clazz, Serializable id) {
        T fetchedObject = null;
        boolean success = false;
        try {
            fetchedObject = session.get(clazz, id);
            if (fetchedObject != null) {
                success = true;
            }
            return fetchedObject;
        } finally {
            auditEngine.generateFor(DataOperation.FETCH, success, fetchedObject, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public CriteriaBuilder criteriaBuilder() {
        return session.getCriteriaBuilder();
    }

    @Override
    public <T extends Model> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return session.createQuery(criteriaQuery);
    }
}