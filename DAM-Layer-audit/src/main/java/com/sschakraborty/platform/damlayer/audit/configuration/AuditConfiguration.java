package com.sschakraborty.platform.damlayer.audit.configuration;

public class AuditConfiguration {
    private int auditIntervalMillis = 5000;
    private int threadPoolSize = 2;
    private byte[] cryptoKey;
    private String secretMask = "*****";

    public AuditConfiguration() {
        int keySize = 50;
        final StringBuilder stringBuilder = new StringBuilder();
        while (keySize-- > 0) {
            stringBuilder.append((char) ((int) ('a' + Math.random() * 26)));
        }
        this.cryptoKey = stringBuilder.toString().getBytes();
    }

    public int getAuditIntervalMillis() {
        return auditIntervalMillis;
    }

    public void setAuditIntervalMillis(int auditIntervalMillis) {
        this.auditIntervalMillis = auditIntervalMillis;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public byte[] getCryptoKey() {
        return cryptoKey;
    }

    public void setCryptoKey(byte[] cryptoKey) {
        this.cryptoKey = cryptoKey;
    }

    public String getSecretMask() {
        return secretMask;
    }

    public void setSecretMask(String secretMask) {
        this.secretMask = secretMask;
    }
}