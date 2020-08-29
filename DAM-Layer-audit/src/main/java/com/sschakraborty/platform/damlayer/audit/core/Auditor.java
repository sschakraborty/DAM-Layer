package com.sschakraborty.platform.damlayer.audit.core;

import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;

import java.util.List;

public interface Auditor {
    void audit(final List<AuditPayload> auditPayloads);
}