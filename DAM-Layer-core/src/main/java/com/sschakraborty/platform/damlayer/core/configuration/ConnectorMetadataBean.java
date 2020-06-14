package com.sschakraborty.platform.damlayer.core.configuration;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Embeddable
public class ConnectorMetadataBean implements ConnectorMetadata {
    @Column(name = "USERNAME", length = 50, nullable = false)
    private String username;

    @Column(name = "PASSWORD", length = 150, nullable = false)
    private String password;

    @Column(name = "AUTO_COMMIT", nullable = false)
    private boolean autoCommit;

    @Column(name = "DEBUG_MODE", nullable = false)
    private boolean debugMode;

    @Column(name = "DIALECT_CLASS", nullable = false)
    private String dialectClass;

    @Column(name = "DRIVER_CLASS", nullable = false)
    private String driverClass;

    @Column(name = "CONN_URL_PREFIX", nullable = false)
    private String connectionURLPrefix;

    @Column(name = "CONN_URL_HOST", nullable = false)
    private String connectionHost;

    @Column(name = "CONN_URL_PORT", nullable = false)
    private int connectionPort;

    @Column(name = "DATABASE_NAME", nullable = false)
    private String databaseName;

    @Column(name = "DDL_MODE", nullable = false)
    private String ddlMode;

    @Column(name = "MAX_POOL_SIZE", nullable = false)
    private int maxPoolSize;

    @Column(name = "IDLE_TIMEOUT", nullable = false)
    private int idleTimeout;

    @Column(name = "CONN_TIMEOUT", nullable = false)
    private int connectionTimeout;

    @PrePersist
    private void prePersist() {
        checkForDefaults();
    }

    @PreUpdate
    private void preUpdate() {
        checkForDefaults();
    }

    private void checkForDefaults() {
        if (ddlMode == null || ddlMode.trim().length() == 0) {
            ddlMode = "auto";
        }
        if (maxPoolSize <= 0) {
            maxPoolSize = 60;
        }
        if (idleTimeout <= 0) {
            idleTimeout = 3000;
        }
        if (connectionTimeout <= 0) {
            connectionTimeout = 10000;
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public String getDialectClass() {
        return dialectClass;
    }

    public void setDialectClass(String dialectClass) {
        this.dialectClass = dialectClass;
    }

    @Override
    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String getConnectionURLPrefix() {
        return connectionURLPrefix;
    }

    public void setConnectionURLPrefix(String connectionURLPrefix) {
        this.connectionURLPrefix = connectionURLPrefix;
    }

    @Override
    public String getConnectionHost() {
        return connectionHost;
    }

    public void setConnectionHost(String connectionHost) {
        this.connectionHost = connectionHost;
    }

    @Override
    public int getConnectionPort() {
        return connectionPort;
    }

    public void setConnectionPort(int connectionPort) {
        this.connectionPort = connectionPort;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String getDdlMode() {
        return ddlMode;
    }

    public void setDdlMode(String ddlMode) {
        this.ddlMode = ddlMode;
    }

    @Override
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}