package com.sschakraborty.platform.damlayer.core.marker;

import com.sschakraborty.platform.damlayer.core.audit.AuditOperation;

public interface Model {
    default String auditModelName() {
        return null;
    }

    default String auditText(AuditOperation auditOperation) {
        return null;
    }
}