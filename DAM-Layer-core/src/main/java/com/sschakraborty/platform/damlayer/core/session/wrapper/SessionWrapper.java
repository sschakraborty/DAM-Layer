package com.sschakraborty.platform.damlayer.core.session.wrapper;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public interface SessionWrapper {
    void insert(String externalText, Object model);

    void update(String externalText, Object model);

    void save(String externalText, Object model);

    void delete(String externalText, Object model);

    <T> T fetch(String externalText, Class<T> clazz, Serializable id);

    <S> CriteriaQuery<S> createCriteriaQuery(Class<S> clazz);

    <S> List<S> executeSelect(CriteriaQuery<S> criteriaQuery, int offset, int limit);

    default <S> List<S> executeSelect(CriteriaQuery<S> criteriaQuery, int limit) {
        return executeSelect(criteriaQuery, 0, limit);
    }

    <S> int executeUpdate(CriteriaQuery<S> criteriaQuery);

    <S> List<S> executeSelect(Class<S> clazz, String jpql, int offset, int limit);
}