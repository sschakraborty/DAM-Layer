package com.sschakraborty.platform.damlayer.core.session.transaction;

import com.sschakraborty.platform.damlayer.audit.core.AuditPayloadGenerator;
import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.core.audit.auditor.AuditPayloadGeneratorImpl;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.session.IsolationMode;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapperImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionManagerImpl implements TransactionManager {
    private final SessionFactory sessionFactory;
    private final TenantConfiguration tenantConfiguration;
    private Auditor auditor;

    public TransactionManagerImpl(
            final SessionFactory sessionFactory,
            final TenantConfiguration tenantConfiguration,
            final Auditor auditor
    ) {
        this.sessionFactory = sessionFactory;
        this.tenantConfiguration = tenantConfiguration;
        this.auditor = auditor;
    }

    public void setAuditor(Auditor auditor) {
        this.auditor = auditor;
    }

    @Override
    public TransactionResult executeStateful(IsolationMode isolationMode, TransactionJob transactionJob) {
        final Session session = buildSession(isolationMode);
        final Transaction transaction = session.beginTransaction();
        final TransactionResultImpl transactionResult = new TransactionResultImpl();
        final AuditPayloadGenerator auditPayloadGenerator = buildAuditPayloadGenerator();
        final SessionWrapper sessionWrapper = new SessionWrapperImpl(session, auditPayloadGenerator);

        final TransactionUnit transactionUnit = new TransactionUnit() {
            @Override
            public SessionWrapper getSession() {
                return sessionWrapper;
            }

            @Override
            public Transaction getTransaction() {
                return transaction;
            }
        };

        try {
            transactionJob.execute(transactionUnit, transactionResult);
            transaction.commit();
            transactionResult.setSuccessful(true);
        } catch (Exception e) {
            transaction.rollback();
            transactionResult.setSuccessful(false);
            transactionResult.setCause(e);
        } finally {
            transactionResult.setTransactionStatus(transaction.getStatus().name());
            session.close();
        }

        auditResource(auditPayloadGenerator);
        return transactionResult;
    }

    @SuppressWarnings("all")
    private Session buildSession(final IsolationMode isolationMode) {
        final Session session = sessionFactory.openSession();
        session.doWork(connection -> connection.setAutoCommit(false));
        session.doWork(connection -> connection.setTransactionIsolation(isolationMode.getIsolationCode()));
        return session;
    }

    private void auditResource(AuditPayloadGenerator auditPayloadGenerator) {
        try {
            if (this.auditor != null && auditPayloadGenerator.shouldAudit()) {
                this.auditor.audit(auditPayloadGenerator.getPayloads());
            }
        } catch (Exception e) {
            // TODO: Log if required
        }
    }

    private AuditPayloadGenerator buildAuditPayloadGenerator() {
        return new AuditPayloadGeneratorImpl(tenantConfiguration);
    }
}