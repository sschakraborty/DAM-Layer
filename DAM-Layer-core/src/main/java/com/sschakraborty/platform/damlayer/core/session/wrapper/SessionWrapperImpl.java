package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
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
            auditEngine.generate(DataOperation.INSERT, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void update(String externalText, Model model) {
        boolean success = false;
        try {
            session.update(model);
            success = true;
        } finally {
            auditEngine.generate(DataOperation.UPDATE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void save(String externalText, Model model) {
        boolean success = false;
        try {
            session.saveOrUpdate(model);
            success = true;
        } finally {
            auditEngine.generate(DataOperation.SAVE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public void delete(String externalText, Model model) {
        boolean success = false;
        try {
            session.delete(model);
            success = true;
        } finally {
            auditEngine.generate(DataOperation.DELETE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        }
    }

    @Override
    public <T extends Model> T fetch(String externalText, Class<T> clazz, Serializable id) {
        T fetchedObject = null;
        try {
            fetchedObject = session.get(clazz, id);
            return fetchedObject;
        } finally {
            if (fetchedObject != null) {
                final DataOperation dataOperation = DataOperation.FETCH;
                auditEngine.generate(dataOperation, true, generateFetchModel(dataOperation, fetchedObject, id), externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
            }
        }
    }

    private <T extends Model> Model generateFetchModel(DataOperation dataOperation, T fetchedObject, Serializable id) {
        String internalText = fetchedObject.getInternalText(dataOperation);
        if (internalText == null) {
            internalText = "Fetched " + fetchedObject.getModelName();
        }
        return new FetchModel(id, fetchedObject.getModelName(), internalText);
    }

    @Override
    public CriteriaBuilder criteriaBuilder() {
        return session.getCriteriaBuilder();
    }

    @Override
    public <T extends Model> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return session.createQuery(criteriaQuery);
    }

    @AuditResource
    private static class FetchModel implements Model {
        private final String modelName;
        private final String internalText;
        @AuditField(identifier = true)
        private Serializable entityFetchId;

        private FetchModel(Serializable entityFetchId, String modelName, String internalText) {
            this.entityFetchId = entityFetchId;
            this.modelName = modelName;
            this.internalText = internalText;
        }

        public Serializable getEntityFetchId() {
            return entityFetchId;
        }

        public void setEntityFetchId(Serializable entityFetchId) {
            this.entityFetchId = entityFetchId;
        }

        public String getInternalText() {
            return internalText;
        }

        @Override
        public String getModelName() {
            return modelName;
        }

        @Override
        public String getInternalText(DataOperation dataOperation) {
            return internalText;
        }
    }
}