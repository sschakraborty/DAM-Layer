package com.sschakraborty.platform.damlayer.core.session.wrapper;

import com.sschakraborty.platform.damlayer.core.audit.AuditOperation;
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
        try {
            session.save(model);
            auditPayloadGenerator.generateFor(AuditOperation.INSERT, true, model, "SYS_CALL");
        } catch (Exception e) {
            auditPayloadGenerator.generateFor(AuditOperation.INSERT, false, model, "SYS_CALL");
            throw e;
        }
    }

    @Override
    public void update(Model model) {
        try {
            session.update(model);
            auditPayloadGenerator.generateFor(AuditOperation.UPDATE, true, model, "SYS_CALL");
        } catch (Exception e) {
            auditPayloadGenerator.generateFor(AuditOperation.UPDATE, false, model, "SYS_CALL");
            throw e;
        }
    }

    @Override
    public void save(Model model) {
        try {
            session.saveOrUpdate(model);
            auditPayloadGenerator.generateFor(AuditOperation.SAVE, true, model, "SYS_CALL");
        } catch (Exception e) {
            auditPayloadGenerator.generateFor(AuditOperation.SAVE, false, model, "SYS_CALL");
            throw e;
        }
    }

    @Override
    public void delete(Model model) {
        try {
            session.delete(model);
            auditPayloadGenerator.generateFor(AuditOperation.DELETE, true, model, "SYS_CALL");
        } catch (Exception e) {
            auditPayloadGenerator.generateFor(AuditOperation.DELETE, false, model, "SYS_CALL");
            throw e;
        }
    }

    @Override
    public <T extends Model> T fetch(Class<T> clazz, Serializable id) {
        return session.get(clazz, id);
    }
}