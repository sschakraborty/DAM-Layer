package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.audit.core.creator.remark.AuditRemarkCreatorProvider;
import com.sschakraborty.platform.damlayer.audit.core.creator.resource.AuditResourceCreatorProvider;
import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.Collections;

public class AuditEngineImpl implements AuditEngine {
    private final Vertx vertx;
    private final Auditor auditor;

    public AuditEngineImpl(final Auditor auditor) {
        this.auditor = auditor;
        this.vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(1).setInternalBlockingPoolSize(1).setEventLoopPoolSize(1));
    }

    @Override
    public void generate(DataOperation dataOperation, boolean successful, Model model, String externalText, String tenantId, String tenantName) {
        this.vertx.executeBlocking(promise -> {
            if (this.isAuditAllowed(model)) {
                final AuditResource auditResource = model.getClass().getAnnotation(AuditResource.class);
                if (auditResource != null) {
                    final AuditPayload auditPayload = new AuditPayload();
                    auditPayload.setDataOperation(dataOperation);
                    auditPayload.setSuccessful(successful);
                    auditPayload.setTenantId(tenantId);
                    auditPayload.setTenantName(tenantName);
                    auditPayload.setClassName(model.getClass().getName());
                    auditPayload.setModelName(generateModelName(model.getClass(), model.getModelName()));
                    auditPayload.setInternalText(generateAuditText(dataOperation, model));
                    auditPayload.setExternalText(externalText);
                    auditPayload.setAuditRemark(getRemark(dataOperation, successful, model, auditResource));
                    auditPayload.setAuditResource(getResource(model, auditResource));
                    auditPayload.setModelObject(model);
                    this.auditor.audit(Collections.singletonList(auditPayload));
                }
            }
        }, result -> {
            // TODO: Log if required
        });
    }

    private String generateAuditText(DataOperation dataOperation, Model model) {
        final String auditText = model.getInternalText(dataOperation);
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

    private boolean isAuditAllowed(Model model) {
        return model != null && !(model instanceof AuditPayload);
    }

    private String getRemark(DataOperation dataOperation, boolean successful, Model model, AuditResource auditResource) {
        return AuditRemarkCreatorProvider.getCreator(auditResource.remarkCreator()).createRemark(dataOperation, model, successful);
    }

    private String getResource(Model model, AuditResource auditResource) {
        return AuditResourceCreatorProvider.getCreator(auditResource.resourceCreator()).createResource(model);
    }
}