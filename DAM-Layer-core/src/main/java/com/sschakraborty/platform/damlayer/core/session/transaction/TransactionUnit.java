package com.sschakraborty.platform.damlayer.core.session.transaction;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import org.hibernate.Transaction;

public interface TransactionUnit {
    SessionWrapper getSession();

    Transaction getTransaction();
}