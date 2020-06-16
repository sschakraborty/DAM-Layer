package com.sschakraborty.platform.damlayer.core.session.transaction;

import java.util.List;

public interface TransactionResult {
    boolean isSuccessful();

    Exception causeOfFailure();

    void put(String key, Object object);

    Object get(String key);

    List<String> keys();

    boolean hasKey(String key);

    String transactionStatus();
}