package com.sschakraborty.platform.damlayer.core.configuration;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.core.session.IsolationMode;

public interface ConnectorMetadata extends AuditModel {
    static ConnectorMetadataBean createBean() {
        return new ConnectorMetadataBean();
    }

    String getUsername();

    String getPassword();

    boolean isAutoCommit();

    boolean isDebugMode();

    String getDialectClass();

    String getDriverClass();

    String getConnectionURLPrefix();

    String getConnectionHost();

    int getConnectionPort();

    String getDatabaseName();

    String getDdlMode();

    int getMaxPoolSize();

    int getIdleTimeout();

    int getConnectionTimeout();

    IsolationMode getIsolationMode();
}