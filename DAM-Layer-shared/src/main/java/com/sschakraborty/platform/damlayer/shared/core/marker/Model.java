package com.sschakraborty.platform.damlayer.shared.core.marker;

import com.sschakraborty.platform.damlayer.shared.audit.AuditOperation;

public interface Model {
    default String auditModelName() {
        return null;
    }

    default String auditText(AuditOperation auditOperation) {
        return null;
    }
}