package com.sschakraborty.platform.damlayer.core.processor;

import com.sschakraborty.platform.damlayer.audit.core.engine.AuditEngine;
import com.sschakraborty.platform.damlayer.audit.payload.DataOperation;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;

import java.io.Serializable;

public class DefaultCallbackHandlerWrapper implements CallbackHandler {
    private final CallbackHandler callbackHandler;
    private final AuditEngine auditEngine;

    public DefaultCallbackHandlerWrapper(CallbackHandler callbackHandler, AuditEngine auditEngine) {
        this.callbackHandler = (callbackHandler == null) ? new CallbackHandler() {
        } : callbackHandler;
        this.auditEngine = auditEngine;
    }

    @Override
    public void preInsert(Object model, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.preInsert(model, externalText, tenantConfiguration);
    }

    @Override
    public void preUpdate(Object model, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.preUpdate(model, externalText, tenantConfiguration);
    }

    @Override
    public void preSave(Object model, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.preSave(model, externalText, tenantConfiguration);
    }

    @Override
    public void preDelete(Object model, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.preDelete(model, externalText, tenantConfiguration);
    }

    @Override
    public void preFetch(Class<Object> clazz, Serializable id, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.preFetch(clazz, id, externalText, tenantConfiguration);
    }

    @Override
    public void postInsert(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
        try {
            auditEngine.generate(DataOperation.INSERT, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        } finally {
            callbackHandler.postInsert(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void postUpdate(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
        try {
            auditEngine.generate(DataOperation.UPDATE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        } finally {
            callbackHandler.postUpdate(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void postSave(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
        try {
            auditEngine.generate(DataOperation.SAVE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        } finally {
            callbackHandler.postSave(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void postDelete(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
        try {
            auditEngine.generate(DataOperation.DELETE, success, model, externalText, tenantConfiguration.getId(), tenantConfiguration.getName());
        } finally {
            callbackHandler.postDelete(model, success, externalText, tenantConfiguration);
        }
    }

    @Override
    public void postFetch(Class<Object> clazz, Serializable id, Object model, String externalText, TenantConfiguration tenantConfiguration) {
        callbackHandler.postFetch(clazz, id, model, externalText, tenantConfiguration);
    }
}