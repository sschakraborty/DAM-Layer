package com.sschakraborty.platform.damlayer.core.configuration;

import com.sschakraborty.platform.damlayer.core.marker.Model;

public interface ConnectorMetadata extends Model {
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