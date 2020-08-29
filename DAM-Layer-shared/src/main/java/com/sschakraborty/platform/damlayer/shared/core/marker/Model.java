package com.sschakraborty.platform.damlayer.shared.core.marker;

import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;

public interface Model {
    default String getModelName() {
        return null;
    }

    default String getInternalText(DataOperation dataOperation) {
        return null;
    }
}