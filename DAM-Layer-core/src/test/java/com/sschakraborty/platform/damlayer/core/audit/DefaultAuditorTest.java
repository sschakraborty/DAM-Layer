package com.sschakraborty.platform.damlayer.core.audit;

import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

public class DefaultAuditorTest {
    @Test
    public void testDefaultAuditorFunctionalities() {
        List<AuditPayload> auditPayloadList = new LinkedList<>();
        TransactionManager transactionManager = Mockito.mock(TransactionManager.class);
        DefaultAuditor defaultAuditor = new DefaultAuditor(transactionManager);
        defaultAuditor.audit(auditPayloadList);
        // Should not throw any exception
        Assert.assertTrue(true);
    }
}