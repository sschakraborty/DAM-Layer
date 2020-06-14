package com.sschakraborty.platform.damlayer.core.session.transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionManagerImpl implements TransactionManager {
    private final SessionFactory sessionFactory;

    public TransactionManagerImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public TransactionResult executeStateful(TransactionJob transactionJob) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final TransactionResultImpl transactionResult = new TransactionResultImpl();

        final TransactionUnit transactionUnit = new TransactionUnit() {
            @Override
            public Session getSession() {
                return session;
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
            session.close();
        }

        return transactionResult;
    }
}