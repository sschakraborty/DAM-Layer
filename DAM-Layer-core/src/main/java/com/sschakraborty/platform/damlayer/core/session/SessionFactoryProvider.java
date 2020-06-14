package com.sschakraborty.platform.damlayer.core.session;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface SessionFactoryProvider {
    static SessionFactory getSessionFactory(final Configuration configuration) {
        return configuration.buildSessionFactory();
    }
}