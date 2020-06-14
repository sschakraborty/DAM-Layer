package com.sschakraborty.platform.damlayer.core.util;

import com.sschakraborty.platform.damlayer.core.session.SessionFactoryProvider;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManagerImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BuilderUtil {
    private BuilderUtil() {
    }

    public static TransactionManager buildTransactionManager(Configuration configuration) {
        final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory(configuration);
        return new TransactionManagerImpl(sessionFactory);
    }
}