package com.sschakraborty.platform.damlayer.core.session;

import java.sql.Connection;

public enum IsolationMode {
    TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),
    TRANSACTION_NONE(Connection.TRANSACTION_NONE);

    private final int isolationCode;

    IsolationMode(int isolationCode) {
        this.isolationCode = isolationCode;
    }

    public int getIsolationCode() {
        return isolationCode;
    }
}