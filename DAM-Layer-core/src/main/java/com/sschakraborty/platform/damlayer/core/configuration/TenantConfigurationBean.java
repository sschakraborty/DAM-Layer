package com.sschakraborty.platform.damlayer.core.configuration;

import javax.persistence.*;

@Entity
@Table(name = "TENANT_CONFIG_METADATA")
public class TenantConfigurationBean implements TenantConfiguration {
    @Id
    @Column(name = "TENANT_ID", nullable = false, length = 25)
    private String id;

    @Column(name = "TENANT_NAME", nullable = false)
    private String name;

    @Embedded
    private ConnectorMetadataBean connectorMetadata;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ConnectorMetadata getConnectorMetadata() {
        return connectorMetadata;
    }

    public void setConnectorMetadata(ConnectorMetadataBean connectorMetadata) {
        this.connectorMetadata = connectorMetadata;
    }
}