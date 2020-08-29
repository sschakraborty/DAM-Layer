package com.sschakraborty.platform.damlayer.audit.core.engine;

import com.sschakraborty.platform.damlayer.shared.audit.AuditOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public interface AuditEngine {
    void generateFor(AuditOperation auditOperation, boolean successful, Model model, String externalText, String tenantId, String tenantName);

    void audit();
}