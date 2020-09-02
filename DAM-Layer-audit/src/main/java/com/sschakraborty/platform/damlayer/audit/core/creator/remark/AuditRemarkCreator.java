package com.sschakraborty.platform.damlayer.audit.core.creator.remark;

import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;

public interface AuditRemarkCreator {
    String createRemark(String modelName, DataOperation dataOperation, Object model, boolean success);
}