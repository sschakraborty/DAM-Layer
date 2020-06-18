package com.sschakraborty.platform.damlayer.core.audit.auditor;

import com.sschakraborty.platform.damlayer.core.audit.AuditPayload;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

import java.util.List;

public class DefaultAuditor implements Auditor {
    private final TransactionManager tenantTransactionManager;

    public DefaultAuditor(TransactionManager tenantTransactionManager) {
        this.tenantTransactionManager = tenantTransactionManager;
    }

    @Override
    public void audit(List<AuditPayload> auditPayloads) {
        tenantTransactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            auditPayloads.forEach(auditPayload -> session.insert("", auditPayload));
        });
    }
}