package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;

import java.io.Serializable;

public interface QueryService {
    default <T extends AuditModel> T fetch(Class<T> clazz, Serializable id) {
        return fetch("Operation performed by Default-SYS-USER", clazz, id);
    }

    default <T extends AuditModel> T fetchTree(Class<T> clazz, Serializable id) {
        return fetchTree("Operation performed by Default-SYS-USER", clazz, id);
    }

    <T extends AuditModel> T fetch(String externalText, Class<T> clazz, Serializable id);

    <T extends AuditModel> T fetchTree(String externalText, Class<T> clazz, Serializable id);
}