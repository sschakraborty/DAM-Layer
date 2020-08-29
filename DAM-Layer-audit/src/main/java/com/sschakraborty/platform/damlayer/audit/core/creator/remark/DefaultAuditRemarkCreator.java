package com.sschakraborty.platform.damlayer.audit.core.creator.remark;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.core.creator.common.FieldAwareConditionalResourceCreator;
import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public class DefaultAuditRemarkCreator extends FieldAwareConditionalResourceCreator implements AuditRemarkCreator {
    private static final AuditFieldConditionPredicate PREDICATE = AuditField::identifier;

    @Override
    public String createRemark(DataOperation dataOperation, Model model, boolean success) {
        switch (dataOperation) {
            case INSERT:
                return createInsertRemark(model, success);
            case UPDATE:
                return createUpdateRemark(model, success);
            case SAVE:
                return createSaveRemark(model, success);
            case DELETE:
                return createDeleteRemark(model, success);
            case FETCH:
                return createFetchRemark(model, success);
        }
        return null;
    }

    private String createFetchRemark(Model model, boolean success) {
        if (success) {
            return String.format("A record of model %s with identifier %s was fetched / read!", model.getModelName(), createResource(model, PREDICATE));
        } else {
            return String.format("Failed to fetch / read a record of model %s with identifier %s!", model.getModelName(), createResource(model, PREDICATE));
        }
    }

    private String createDeleteRemark(Model model, boolean success) {
        if (success) {
            return String.format("The record of model %s with identifier %s was deleted!", model.getModelName(), createResource(model, PREDICATE));
        } else {
            return String.format("Failed to delete record of model %s with identifier %s!", model.getModelName(), createResource(model, PREDICATE));
        }
    }

    private String createSaveRemark(Model model, boolean success) {
        if (success) {
            return String.format("Record of model %s with identifier %s was either created / inserted or updated!", model.getModelName(), createResource(model, PREDICATE));
        } else {
            return String.format("Failed to create / insert or update record of model %s with identifier %s!", model.getModelName(), createResource(model, PREDICATE));
        }
    }

    private String createUpdateRemark(Model model, boolean success) {
        if (success) {
            return String.format("The record of model %s with identifier %s was updated!", model.getModelName(), createResource(model, PREDICATE));
        } else {
            return String.format("Failed to update record of model %s with identifier %s!", model.getModelName(), createResource(model, PREDICATE));
        }
    }

    private String createInsertRemark(Model model, boolean success) {
        if (success) {
            return String.format("A new record of model %s with identifier %s was inserted / created!", model.getModelName(), createResource(model, PREDICATE));
        } else {
            return String.format("Failed to create / insert a new record of model %s with identifier %s!", model.getModelName(), createResource(model, PREDICATE));
        }
    }
}