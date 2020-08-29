package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.io.Serializable;

public interface QueryService {
    default <T extends Model> T fetch(Class<T> clazz, Serializable id) {
        return fetch("Operation performed by Default-SYS-USER", clazz, id);
    }

    default <T extends Model> T fetchTree(Class<T> clazz, Serializable id) {
        return fetchTree("Operation performed by Default-SYS-USER", clazz, id);
    }

    <T extends Model> T fetch(String externalText, Class<T> clazz, Serializable id);

    <T extends Model> T fetchTree(String externalText, Class<T> clazz, Serializable id);
}