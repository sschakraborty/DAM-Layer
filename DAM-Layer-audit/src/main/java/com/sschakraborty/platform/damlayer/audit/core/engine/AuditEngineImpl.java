package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.audit.configuration.AuditConfiguration;
import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.audit.core.creator.remark.AuditRemarkCreatorProvider;
import com.sschakraborty.platform.damlayer.audit.core.creator.resource.AuditResourceCreatorProvider;
import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuditEngineImpl implements AuditEngine {
    private static final boolean SERIAL_EXECUTION = false;
    private final AuditConfiguration auditConfiguration;
    private final Vertx vertx;
    private final Auditor auditor;
    private final List<AuditPayload> auditPayloads;

    public AuditEngineImpl(final Auditor auditor, final AuditConfiguration auditConfiguration) {
        this.vertx = Vertx.vertx(
                new VertxOptions()
                        .setWorkerPoolSize(auditConfiguration.getThreadPoolSize())
                        .setMaxWorkerExecuteTime(10000)
                        .setMaxWorkerExecuteTimeUnit(TimeUnit.MILLISECONDS)
                        .setEventLoopPoolSize(1)
                        .setInternalBlockingPoolSize(1)
        );
        this.auditConfiguration = auditConfiguration;
        this.auditor = auditor;
        this.auditPayloads = new LinkedList<>();
        this.initializeHooks();
    }

    @Override
    public void generate(DataOperation dataOperation, boolean successful, Model baseModel, String externalText, String tenantId, String tenantName) {
        this.vertx.executeBlocking(promise -> {
            if (this.isAuditAllowed(baseModel)) {
                final AuditResource auditResource = baseModel.getClass().getAnnotation(AuditResource.class);
                if (auditResource != null && auditResource.enabled()) {
                    final List<Model> fieldModels = fetchModelFields(baseModel);
                    fieldModels.forEach(model -> {
                        final AuditPayload auditPayload = generateAuditPayload(dataOperation, successful, externalText, tenantId, tenantName, auditResource, model);
                        synchronized (this.auditPayloads) {
                            this.auditPayloads.add(auditPayload);
                        }
                    });
                }
            }
        }, SERIAL_EXECUTION, result -> {
            // TODO: Log if required
        });
    }

    private AuditPayload generateAuditPayload(DataOperation dataOperation, boolean successful, String externalText, String tenantId, String tenantName, AuditResource auditResource, Model model) {
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
        return auditPayload;
    }

    private List<Model> fetchModelFields(Model model) {
        final List<Model> models = new LinkedList<>();
        models.add(model);
        for (Field field : model.getClass().getDeclaredFields()) {
            final AuditField auditField = field.getAnnotation(AuditField.class);
            if (auditField != null && auditField.enabled()) {
                try {
                    field.setAccessible(true);
                    final Object object = field.get(model);
                    if (object instanceof Collection) {
                        final Collection<?> collection = (Collection<?>) object;
                        for (final Object collectionObj : collection) {
                            populateModels(models, collectionObj);
                        }
                    } else {
                        populateModels(models, object);
                    }
                } catch (IllegalAccessException ignored) {
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return models;
    }

    private void populateModels(List<Model> models, Object object) {
        if (object instanceof Model) {
            final AuditResource auditResource = object.getClass().getAnnotation(AuditResource.class);
            if (auditResource != null && auditResource.enabled()) {
                models.add((Model) object);
            }
        }
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
        this.vertx.setPeriodic(
                this.auditConfiguration.getAuditIntervalMillis(),
                id -> this.vertx.executeBlocking(promise -> this.audit(), SERIAL_EXECUTION, result -> {
                })
        );
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
        return AuditRemarkCreatorProvider.getCreator(this.auditConfiguration, auditResource.remarkCreator()).createRemark(dataOperation, model, successful);
    }

    private String getResource(Model model, AuditResource auditResource) {
        return AuditResourceCreatorProvider.getCreator(this.auditConfiguration, auditResource.resourceCreator()).createResource(model);
    }
}