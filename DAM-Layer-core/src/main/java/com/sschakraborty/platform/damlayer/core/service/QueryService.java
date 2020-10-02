package com.sschakraborty.platform.damlayer.core.service;

import java.io.Serializable;

public interface QueryService {
    default <T> T fetch(Class<T> clazz, Serializable id) {
        return fetch("Operation performed by Default-SYS-USER", clazz, id);
    }

    default <T> T fetchTree(Class<T> clazz, Serializable id) {
        return fetchTree("Operation performed by Default-SYS-USER", clazz, id);
    }

    <T> T fetch(String externalText, Class<T> clazz, Serializable id);

    <T> T fetchTree(String externalText, Class<T> clazz, Serializable id);
}