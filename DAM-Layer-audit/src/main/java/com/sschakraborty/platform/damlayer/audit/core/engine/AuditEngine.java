package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.audit.payload.DataOperation;

public interface AuditEngine {
    default void generate(DataOperation dataOperation, boolean successful, Object model, String externalText, String tenantId, String tenantName) {
        if (model instanceof AuditModel) {
            generate(dataOperation, successful, (AuditModel) model, externalText, tenantId, tenantName);
        }
    }

    void generate(DataOperation dataOperation, boolean successful, AuditModel model, String externalText, String tenantId, String tenantName);
}