package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.io.Serializable;

public interface SessionWrapper {
    void insert(String externalText, Model model);

    void update(String externalText, Model model);

    void save(String externalText, Model model);

    void delete(String externalText, Model model);

    <T extends Model> T fetch(Class<T> clazz, Serializable id);
}