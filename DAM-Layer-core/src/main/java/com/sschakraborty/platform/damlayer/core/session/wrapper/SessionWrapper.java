package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public interface SessionWrapper {
    void insert(String externalText, Model model);

    void update(String externalText, Model model);

    void save(String externalText, Model model);

    void delete(String externalText, Model model);

    <T extends Model> T fetch(Class<T> clazz, Serializable id);

    CriteriaBuilder criteriaBuilder();

    <T extends Model> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);
}