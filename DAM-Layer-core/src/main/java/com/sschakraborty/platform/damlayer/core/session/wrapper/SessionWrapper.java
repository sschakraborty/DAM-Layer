package com.sschakraborty.platform.damlayer.core.session.wrapper;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public interface SessionWrapper {
    void insert(String externalText, Object model);

    void update(String externalText, Object model);

    void save(String externalText, Object model);

    void delete(String externalText, Object model);

    <T> T fetch(String externalText, Class<T> clazz, Serializable id);

    CriteriaBuilder criteriaBuilder();

    <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);
}