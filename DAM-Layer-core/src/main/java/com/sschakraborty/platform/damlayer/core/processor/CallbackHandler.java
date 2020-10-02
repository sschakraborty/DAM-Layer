package com.sschakraborty.platform.damlayer.core.processor;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;

import java.io.Serializable;

public interface CallbackHandler {
    default void preInsert(Object model, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void preUpdate(Object model, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void preSave(Object model, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void preDelete(Object model, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void preFetch(Class<Object> clazz, Serializable id, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void postInsert(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void postUpdate(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void postSave(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void postDelete(Object model, boolean success, String externalText, TenantConfiguration tenantConfiguration) {
    }

    default void postFetch(Class<Object> clazz, Serializable id, Object model, String externalText, TenantConfiguration tenantConfiguration) {
    }
}