package com.sschakraborty.platform.damlayer.audit.payload;

import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "AUDIT_RESOURCE")
public class AuditPayload implements Model {
    @Id
    @Column(name = "AUDIT_ID", nullable = false)
    private String auditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION", nullable = false)
    private DataOperation dataOperation;

    @Column(name = "SUCCESSFUL", nullable = false)
    private boolean successful;

    @Column(name = "DATE_TIME", nullable = false)
    private Timestamp timestamp = Timestamp.from(Instant.now());

    @Column(name = "TENANT_ID", nullable = false)
    private String tenantId;

    @Column(name = "TENANT_NAME", nullable = false)
    private String tenantName;

    @Column(name = "CLASS_NAME", nullable = false)
    private String className;

    @Column(name = "MODEL_NAME", nullable = false)
    private String modelName;

    @Column(name = "INTERNAL_TEXT", nullable = false, length = 2000)
    private String internalText;

    @Column(name = "EXTERNAL_TEXT", length = 2000)
    private String externalText;

    @Lob
    @Column(name = "AUDIT_REMARK")
    private String auditRemark;

    @Lob
    @Column(name = "AUDIT_RESOURCE")
    private String auditResource;

    @Transient
    private transient Model modelObject;

    public AuditPayload() {
        this.generateAuditId();
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public DataOperation getDataOperation() {
        return dataOperation;
    }

    public void setDataOperation(DataOperation dataOperation) {
        this.dataOperation = dataOperation;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getInternalText() {
        return internalText;
    }

    public void setInternalText(String internalText) {
        this.internalText = internalText;
    }

    public String getExternalText() {
        return externalText;
    }

    public void setExternalText(String externalText) {
        this.externalText = externalText;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditResource() {
        return auditResource;
    }

    public void setAuditResource(String auditResource) {
        this.auditResource = auditResource;
    }

    public Model getModelObject() {
        return modelObject;
    }

    public void setModelObject(Model modelObject) {
        this.modelObject = modelObject;
    }

    private void generateAuditId() {
        final int suffixLength = 25;
        final StringBuilder id = new StringBuilder();
        id.append(this.getTimestamp()).append("-");
        for (int counter = 0; counter < suffixLength; counter++) {
            id.append((char) ('A' + ((int) (Math.random() * 26))));
        }
        setAuditId(id.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditPayload that = (AuditPayload) o;

        if (isSuccessful() != that.isSuccessful()) return false;
        if (getDataOperation() != that.getDataOperation()) return false;
        if (!getTimestamp().equals(that.getTimestamp())) return false;
        if (!getTenantId().equals(that.getTenantId())) return false;
        if (!getTenantName().equals(that.getTenantName())) return false;
        if (!getClassName().equals(that.getClassName())) return false;
        if (!getModelName().equals(that.getModelName())) return false;
        if (!getInternalText().equals(that.getInternalText())) return false;
        return getExternalText().equals(that.getExternalText());
    }

    @Override
    public int hashCode() {
        int result = getDataOperation().hashCode();
        result = 31 * result + (isSuccessful() ? 1 : 0);
        result = 31 * result + getTimestamp().hashCode();
        result = 31 * result + getTenantId().hashCode();
        result = 31 * result + getTenantName().hashCode();
        result = 31 * result + getClassName().hashCode();
        result = 31 * result + getModelName().hashCode();
        result = 31 * result + getInternalText().hashCode();
        result = 31 * result + getExternalText().hashCode();
        return result;
    }
}