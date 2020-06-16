package com.sschakraborty.platform.damlayer.core.audit.auditor;

import com.sschakraborty.platform.damlayer.core.audit.AuditOperation;
import com.sschakraborty.platform.damlayer.core.audit.AuditPayload;
import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.util.List;

public interface AuditPayloadGenerator {
    default boolean isAuditAllowed(Model model) {
        return !(model instanceof AuditPayload);
    }

    void generateFor(AuditOperation auditOperation, boolean successful, Model model, String externalText);

    List<AuditPayload> getPayloads();
}