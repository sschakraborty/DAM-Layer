package com.sschakraborty.platform.damlayer.core.session.transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface TransactionUnit {
    Session getSession();

    Transaction getTransaction();
}