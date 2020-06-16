package com.sschakraborty.platform.damlayer.core.marker;

import com.sschakraborty.platform.damlayer.core.audit.AuditOperation;

public interface Model {
    default String getModelName() {
        return null;
    }

    default String getAuditText(AuditOperation auditOperation) {
        return null;
    }
}