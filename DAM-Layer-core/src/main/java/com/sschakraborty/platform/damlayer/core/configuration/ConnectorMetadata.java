package com.sschakraborty.platform.damlayer.core.configuration;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.core.session.IsolationMode;

import javax.persistence.*;

@Embeddable
public class ConnectorMetadata implements AuditModel {
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

    @Column(name = "CONN_ISOLATION", nullable = false)
    @Enumerated(EnumType.STRING)
    private IsolationMode isolationMode = IsolationMode.TRANSACTION_READ_COMMITTED;

    public static ConnectorMetadata createBean() {
        return new ConnectorMetadata();
    }

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
        if (isolationMode == null) {
            isolationMode = IsolationMode.TRANSACTION_READ_COMMITTED;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getDialectClass() {
        return dialectClass;
    }

    public void setDialectClass(String dialectClass) {
        this.dialectClass = dialectClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getConnectionURLPrefix() {
        return connectionURLPrefix;
    }

    public void setConnectionURLPrefix(String connectionURLPrefix) {
        this.connectionURLPrefix = connectionURLPrefix;
    }

    public String getConnectionHost() {
        return connectionHost;
    }

    public void setConnectionHost(String connectionHost) {
        this.connectionHost = connectionHost;
    }

    public int getConnectionPort() {
        return connectionPort;
    }

    public void setConnectionPort(int connectionPort) {
        this.connectionPort = connectionPort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDdlMode() {
        return ddlMode;
    }

    public void setDdlMode(String ddlMode) {
        this.ddlMode = ddlMode;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public IsolationMode getIsolationMode() {
        return isolationMode;
    }

    public void setIsolationMode(IsolationMode isolationMode) {
        this.isolationMode = isolationMode;
        if (this.isolationMode == null) {
            this.isolationMode = IsolationMode.TRANSACTION_READ_COMMITTED;
        }
    }
}