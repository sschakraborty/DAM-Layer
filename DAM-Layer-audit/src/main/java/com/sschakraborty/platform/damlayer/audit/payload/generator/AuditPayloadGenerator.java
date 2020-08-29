package com.sschakraborty.platform.damlayer.audit.payload.generator;

import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.shared.audit.AuditOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.util.List;

public interface AuditPayloadGenerator {
    default boolean isAuditAllowed(Model model) {
        return !(model instanceof AuditPayload);
    }

    void generateFor(AuditOperation auditOperation, boolean successful, Model model, String externalText);

    List<AuditPayload> getPayloads();

    boolean shouldAudit();
}