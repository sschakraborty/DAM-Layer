package com.sschakraborty.platform.damlayer.audit.core;

import java.util.List;

public interface Auditor {
    void audit(final List<AuditPayload> auditPayloads);
}