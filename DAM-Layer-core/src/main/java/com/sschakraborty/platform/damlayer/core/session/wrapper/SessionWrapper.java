package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

public interface SessionWrapper {
    void insert(String externalText, Model model);

    void update(String externalText, Model model);

    void save(String externalText, Model model);

    void delete(String externalText, Model model);

    <T extends AuditModel> T fetch(String externalText, Class<T> clazz, Serializable id);

    CriteriaBuilder criteriaBuilder();

    <T extends AuditModel> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);
}