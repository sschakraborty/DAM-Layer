package com.sschakraborty.platform.damlayer.audit.core;

import com.sschakraborty.platform.damlayer.audit.payload.DataOperation;

public interface AuditModel {
    default String getModelName() {
        return this.getClass().getName();
    }

    default String getInternalText(DataOperation dataOperation) {
        return null;
    }
}