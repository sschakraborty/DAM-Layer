package com.sschakraborty.platform.damlayer.audit.payload.generator;

import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.shared.audit.AuditOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.util.LinkedList;
import java.util.List;

public class AuditPayloadGeneratorImpl implements AuditPayloadGenerator {
    private final List<AuditPayload> auditPayloads = new LinkedList<>();
    private final String tenantId;
    private final String tenantName;

    public AuditPayloadGeneratorImpl(String tenantId, String tenantName) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }

    @Override
    public void generateFor(AuditOperation auditOperation, boolean successful, Model model, String externalText) {
        if (this.isAuditAllowed(model)) {
            final AuditPayload auditPayload = new AuditPayload();
            auditPayload.setAuditOperation(auditOperation);
            auditPayload.setSuccessful(successful);
            auditPayload.setTenantId(tenantId);
            auditPayload.setTenantName(tenantName);
            auditPayload.setClassName(model.getClass().getName());
            auditPayload.setModelName(generateModelName(model.getClass(), model.auditModelName()));
            auditPayload.setAuditText(generateAuditText(auditOperation, model));
            auditPayload.setExternalText(externalText);
            auditPayload.setModelObject(model);
            this.auditPayloads.add(auditPayload);
        }
    }

    private String generateAuditText(AuditOperation auditOperation, Model model) {
        final String auditText = model.auditText(auditOperation);
        if (auditText != null && auditText.trim().length() != 0) {
            return auditText;
        }
        return model.toString();
    }

    private String generateModelName(Class<? extends Model> clazz, String modelName) {
        if (modelName == null || modelName.trim().length() == 0) {
            return clazz.getSimpleName();
        }
        return modelName;
    }

    @Override
    public List<AuditPayload> getPayloads() {
        return this.auditPayloads;
    }

    @Override
    public boolean shouldAudit() {
        return !this.auditPayloads.isEmpty();
    }
}
