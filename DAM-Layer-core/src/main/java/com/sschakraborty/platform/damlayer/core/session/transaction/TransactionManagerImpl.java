package com.sschakraborty.platform.damlayer.core.session.transaction;

import com.sschakraborty.platform.damlayer.audit.core.engine.AuditEngine;
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
    private AuditEngine auditEngine;

    public TransactionManagerImpl(SessionFactory sessionFactory, TenantConfiguration tenantConfiguration, AuditEngine auditEngine) {
        this.sessionFactory = sessionFactory;
        this.tenantConfiguration = tenantConfiguration;
        this.auditEngine = auditEngine;
    }

    public void setAuditEngine(AuditEngine auditEngine) {
        this.auditEngine = auditEngine;
    }

    @Override
    public TransactionResult executeStateful(IsolationMode isolationMode, TransactionJob transactionJob) {
        final Session session = buildSession(isolationMode);
        final Transaction transaction = session.beginTransaction();
        final TransactionResultImpl transactionResult = new TransactionResultImpl();
        final SessionWrapper sessionWrapper = new SessionWrapperImpl(session, auditEngine, tenantConfiguration);
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
            auditEngine.audit();
        }
        return transactionResult;
    }

    @SuppressWarnings("all")
    private Session buildSession(final IsolationMode isolationMode) {
        final Session session = sessionFactory.openSession();
        session.doWork(connection -> connection.setAutoCommit(false));
        session.doWork(connection -> connection.setTransactionIsolation(isolationMode.getIsolationCode()));
        return session;
    }
}