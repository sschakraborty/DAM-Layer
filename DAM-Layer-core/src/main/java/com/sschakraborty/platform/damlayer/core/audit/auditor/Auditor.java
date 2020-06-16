package com.sschakraborty.platform.damlayer.core.audit.auditor;

import com.sschakraborty.platform.damlayer.core.audit.AuditPayload;

import java.util.List;

public interface Auditor {
    void audit(final List<AuditPayload> auditPayloads);
}