package com.sschakraborty.platform.damlayer.core.session.transaction;

import java.util.*;

public class TransactionResultImpl implements TransactionResult {
    private final Map<String, Object> responseMap;
    private boolean successful;
    private Exception cause;
    private String transactionStatus;

    public TransactionResultImpl() {
        cause = null;
        responseMap = new HashMap<>();
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public void setCause(Exception cause) {
        this.cause = cause;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public Exception causeOfFailure() {
        return cause;
    }

    @Override
    public void put(String key, Object object) {
        this.responseMap.put(key, object);
    }

    @Override
    public Object get(String key) {
        return this.responseMap.get(key);
    }

    @Override
    public List<String> keys() {
        final Set<String> keySet = this.responseMap.keySet();
        final List<String> keys = new ArrayList<>(keySet.size());
        keys.addAll(keySet);
        return keys;
    }

    @Override
    public boolean hasKey(String key) {
        return this.responseMap.containsKey(key);
    }

    @Override
    public String transactionStatus() {
        return this.transactionStatus;
    }
}