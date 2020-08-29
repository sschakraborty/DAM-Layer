package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.shared.audit.AuditOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.util.LinkedList;
import java.util.List;

public class AuditEngineImpl implements AuditEngine {
    private final Auditor auditor;
    private List<AuditPayload> auditPayloads;

    public AuditEngineImpl(final Auditor auditor) {
        this.auditor = auditor;
        this.audit();
    }

    @Override
    public void generateFor(AuditOperation auditOperation, boolean successful, Model model, String externalText, String tenantId, String tenantName) {
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

    @Override
    public void audit() {
        try {
            if (this.shouldAudit()) {
                this.auditor.audit(this.auditPayloads);
            }
        } catch (Exception e) {
            // TODO: Log if required
        } finally {
            this.auditPayloads = new LinkedList<>();
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

    private boolean shouldAudit() {
        return this.auditPayloads != null && !this.auditPayloads.isEmpty();
    }

    private boolean isAuditAllowed(Model model) {
        return !(model instanceof AuditPayload);
    }
}