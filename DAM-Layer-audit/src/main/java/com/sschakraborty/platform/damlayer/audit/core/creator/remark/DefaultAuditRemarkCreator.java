package com.sschakraborty.platform.damlayer.audit.core.creator.remark;

import com.sschakraborty.platform.damlayer.audit.configuration.AuditConfiguration;
import com.sschakraborty.platform.damlayer.audit.core.creator.common.FieldAwareConditionalResourceCreator;
import com.sschakraborty.platform.damlayer.audit.payload.DataOperation;
import io.vertx.core.json.Json;

public class DefaultAuditRemarkCreator extends FieldAwareConditionalResourceCreator implements AuditRemarkCreator {
    public DefaultAuditRemarkCreator(final AuditConfiguration auditConfiguration) {
        super(auditConfiguration.getCryptoKey(), auditConfiguration.getSecretMask());
    }

    @Override
    public String createRemark(String modelName, DataOperation dataOperation, Object model, boolean success) {
        switch (dataOperation) {
            case INSERT:
                return createInsertRemark(modelName, model, success);
            case UPDATE:
                return createUpdateRemark(modelName, model, success);
            case SAVE:
                return createSaveRemark(modelName, model, success);
            case DELETE:
                return createDeleteRemark(modelName, model, success);
        }
        return null;
    }

    private String createIdentifierFromModel(Object model) {
        return Json.encode(super.createIdentifier(model));
    }

    private String createDeleteRemark(String modelName, Object model, boolean success) {
        if (success) {
            return String.format("The record of model %s with identifier %s was deleted!", modelName, createIdentifierFromModel(model));
        } else {
            return String.format("Failed to delete record of model %s with identifier %s!", modelName, createIdentifierFromModel(model));
        }
    }

    private String createSaveRemark(String modelName, Object model, boolean success) {
        if (success) {
            return String.format("Record of model %s with identifier %s was either created / inserted or updated!", modelName, createIdentifierFromModel(model));
        } else {
            return String.format("Failed to create / insert or update record of model %s with identifier %s!", modelName, createIdentifierFromModel(model));
        }
    }

    private String createUpdateRemark(String modelName, Object model, boolean success) {
        if (success) {
            return String.format("The record of model %s with identifier %s was updated!", modelName, createIdentifierFromModel(model));
        } else {
            return String.format("Failed to update record of model %s with identifier %s!", modelName, createIdentifierFromModel(model));
        }
    }

    private String createInsertRemark(String modelName, Object model, boolean success) {
        if (success) {
            return String.format("A new record of model %s with identifier %s was inserted / created!", modelName, createIdentifierFromModel(model));
        } else {
            return String.format("Failed to create / insert a new record of model %s with identifier %s!", modelName, createIdentifierFromModel(model));
        }
    }
}