package com.sschakraborty.platform.damlayer.audit.core.creator.resource;

import com.sschakraborty.platform.damlayer.audit.configuration.AuditConfiguration;
import com.sschakraborty.platform.damlayer.audit.core.creator.common.FieldAwareConditionalResourceCreator;
import io.vertx.core.json.Json;

public class DefaultAuditResourceCreator extends FieldAwareConditionalResourceCreator implements AuditResourceCreator {
    public DefaultAuditResourceCreator(final AuditConfiguration auditConfiguration) {
        super(auditConfiguration.getCryptoKey(), auditConfiguration.getSecretMask());
    }

    @Override
    public String createResource(Object model) {
        return Json.encode(super.createResource(model));
    }
}