package com.sschakraborty.platform.damlayer.core.configuration;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;

import javax.persistence.*;
import java.io.*;
import java.util.List;

@Entity
@Table(name = "TENANT_CONFIG_METADATA")
@AuditResource
public class TenantConfiguration {
    @Id
    @Column(name = "TENANT_ID", nullable = false, length = 25)
    @AuditField(identifier = true)
    private String id;

    @Column(name = "TENANT_NAME", nullable = false)
    @AuditField(identifier = true)
    private String name;

    @Embedded
    private ConnectorMetadata connectorMetadata;

    @Lob
    @Column(name = "CLASSES_BIN_BYTES", nullable = false)
    private byte[] annotatedClassesBytes;

    @Transient
    private transient List<Class<?>> annotatedClasses;

    public static TenantConfiguration createBean() {
        return new TenantConfiguration();
    }

    /**
     * Unique identifier for the tenant (used as a key for cache)
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * This is used to get a (displayable) name for the tenant
     *
     * @return Name of the tenant
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the connector metadata for tenant
     *
     * @return Connector metadata for tenant
     */
    public ConnectorMetadata getConnectorMetadata() {
        return connectorMetadata;
    }

    public void setConnectorMetadata(ConnectorMetadata connectorMetadata) {
        this.connectorMetadata = connectorMetadata;
    }

    public byte[] getAnnotatedClassesBytes() {
        return annotatedClassesBytes;
    }

    public void setAnnotatedClassesBytes(byte[] annotatedClassesBytes) {
        this.annotatedClassesBytes = annotatedClassesBytes;
    }

    /**
     * Returns the list of annotated classes
     */
    public List<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public void setAnnotatedClasses(List<Class<?>> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    @PrePersist
    @PreUpdate
    @PreRemove
    private void createClassesBytes() throws IOException {
        try (final ByteArrayOutputStream byteWriter = new ByteArrayOutputStream()) {
            try (final ObjectOutputStream classWriter = new ObjectOutputStream(byteWriter)) {
                if (annotatedClasses != null) {
                    classWriter.writeObject(annotatedClasses);
                }
            }
            annotatedClassesBytes = byteWriter.toByteArray();
        }
    }

    @PostLoad
    @SuppressWarnings("unchecked")
    private void createClassesList() throws IOException, ClassNotFoundException {
        try (final ByteArrayInputStream byteReader = new ByteArrayInputStream(annotatedClassesBytes)) {
            try (final ObjectInputStream objectReader = new ObjectInputStream(byteReader)) {
                annotatedClasses = (List<Class<?>>) objectReader.readObject();
            }
        }
    }
}