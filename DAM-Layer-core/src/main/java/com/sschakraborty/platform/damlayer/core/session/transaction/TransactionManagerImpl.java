package com.sschakraborty.platform.damlayer.core.session.transaction;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapperImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionManagerImpl implements TransactionManager {
    private final SessionFactory sessionFactory;
    private final TenantConfiguration tenantConfiguration;

    public TransactionManagerImpl(final SessionFactory sessionFactory, final TenantConfiguration tenantConfiguration) {
        this.sessionFactory = sessionFactory;
        this.tenantConfiguration = tenantConfiguration;
    }

    @Override
    public TransactionResult executeStateful(TransactionJob transactionJob) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final TransactionResultImpl transactionResult = new TransactionResultImpl();
        final SessionWrapper sessionWrapper = new SessionWrapperImpl(session);

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

        return transactionResult;
    }
}