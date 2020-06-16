package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.io.Serializable;

public interface SessionWrapper {
    void insert(Model model);

    void update(Model model);

    void save(Model model);

    void delete(Model model);

    <T extends Model> T fetch(Class<T> clazz, Serializable id);
}