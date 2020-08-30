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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuditEngineImpl implements AuditEngine {
    private static final int AUDIT_INTERVAL_MILLIS = 5000;
    private static final boolean SERIAL_EXECUTION = false;
    private final Vertx vertx;
    private final Auditor auditor;
    private final List<AuditPayload> auditPayloads;

    public AuditEngineImpl(final Auditor auditor) {
        this.vertx = Vertx.vertx(
                new VertxOptions()
                        .setWorkerPoolSize(2)
                        .setMaxWorkerExecuteTime(10000)
                        .setMaxWorkerExecuteTimeUnit(TimeUnit.MILLISECONDS)
                        .setEventLoopPoolSize(1)
                        .setInternalBlockingPoolSize(1)
        );
        this.auditor = auditor;
        this.auditPayloads = new LinkedList<>();
        this.initializeHooks();
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
                    synchronized (this.auditPayloads) {
                        this.auditPayloads.add(auditPayload);
                    }
                }
            }
        }, SERIAL_EXECUTION, result -> {
            // TODO: Log if required
        });
    }

    private void audit() {
        try {
            synchronized (this.auditor) {
                final List<AuditPayload> copyList;
                synchronized (this.auditPayloads) {
                    copyList = new ArrayList<>(this.auditPayloads.size());
                    copyList.addAll(this.auditPayloads);
                    this.auditPayloads.clear();
                }
                if (!copyList.isEmpty()) {
                    this.auditor.audit(copyList);
                }
            }
        } catch (Exception e) {
            // TODO: Log if required
        }
    }

    private void initializeHooks() {
        this.vertx.setPeriodic(AUDIT_INTERVAL_MILLIS, id -> this.vertx.executeBlocking(promise -> this.audit(), SERIAL_EXECUTION, result -> {
        }));
        Runtime.getRuntime().addShutdownHook(new Thread(this::audit));
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