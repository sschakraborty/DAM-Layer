package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.audit.auditor.AuditPayloadGenerator;
import com.sschakraborty.platform.damlayer.core.marker.Model;
import org.hibernate.Session;

import java.io.Serializable;

public class SessionWrapperImpl implements SessionWrapper {
    private final Session session;
    private final AuditPayloadGenerator auditPayloadGenerator;

    public SessionWrapperImpl(Session session, AuditPayloadGenerator auditPayloadGenerator) {
        this.session = session;
        this.auditPayloadGenerator = auditPayloadGenerator;
    }

    @Override
    public void insert(Model model) {
        session.save(model);
    }

    @Override
    public void update(Model model) {
        session.update(model);
    }

    @Override
    public void save(Model model) {
        session.saveOrUpdate(model);
    }

    @Override
    public void delete(Model model) {
        session.delete(model);
    }

    @Override
    public <T extends Model> T fetch(Class<T> clazz, Serializable id) {
        return session.get(clazz, id);
    }
}