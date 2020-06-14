package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.io.Serializable;

public interface QueryService {
    <T extends Model> T fetch(Class<T> clazz, Serializable id);

    <T extends Model> T fetchTree(Class<T> clazz, Serializable id);
}