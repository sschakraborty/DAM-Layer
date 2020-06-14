package com.sschakraborty.platform.damlayer.core.configuration;

import java.io.Serializable;

public interface ConnectorMetadata extends Serializable {
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
}