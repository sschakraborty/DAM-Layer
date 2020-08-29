package com.sschakraborty.platform.damlayer.audit.core.creator.resource;

import com.sschakraborty.platform.damlayer.audit.core.creator.common.FieldAwareConditionalResourceCreator;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public class DefaultAuditResourceCreator extends FieldAwareConditionalResourceCreator implements AuditResourceCreator {
    private static final AuditFieldConditionPredicate PREDICATE = auditField -> true;

    @Override
    public String createResource(Model model) {
        return createResource(model, PREDICATE);
    }
}