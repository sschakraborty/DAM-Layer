package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public interface AuditEngine {
    void generate(DataOperation dataOperation, boolean successful, Model model, String externalText, String tenantId, String tenantName);
}