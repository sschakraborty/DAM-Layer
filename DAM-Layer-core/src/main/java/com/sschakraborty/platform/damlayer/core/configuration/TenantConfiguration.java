package com.sschakraborty.platform.damlayer.core.configuration;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.util.List;

public interface TenantConfiguration extends Model {
    static TenantConfigurationBean createBean() {
        return new TenantConfigurationBean();
    }

    /**
     * Unique identifier for the tenant (used as a key for cache)
     *
     * @return id
     */
    String getId();

    /**
     * This is used to get a (displayable) name for the tenant
     *
     * @return Name of the tenant
     */
    String getName();

    /**
     * Returns the connector metadata for tenant
     *
     * @return Connector metadata for tenant
     */
    ConnectorMetadata getConnectorMetadata();

    /**
     * Returns the list of annotated classes
     */
    List<Class<? extends Model>> getAnnotatedClasses();
}