package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.audit.payload.DataOperation;

public interface AuditEngine {
    void generate(DataOperation dataOperation, boolean successful, AuditModel model, String externalText, String tenantId, String tenantName);
}