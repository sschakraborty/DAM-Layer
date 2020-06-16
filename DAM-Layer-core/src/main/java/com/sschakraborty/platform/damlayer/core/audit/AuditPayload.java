package com.sschakraborty.platform.damlayer.core.audit;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "AUDIT_RESOURCE")
public class AuditPayload implements Model {
    @Id
    @Column(name = "AUDIT_ID", nullable = false)
    private String auditId;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION", nullable = false)
    private AuditOperation auditOperation;

    @Column(name = "SUCCESSFUL", nullable = false)
    private boolean successful;

    @Column(name = "DATE_TIME", nullable = false)
    private Date dateTime = Date.valueOf(LocalDate.now());

    @Column(name = "TENANT_ID", nullable = false)
    private String tenantId;

    @Column(name = "TENANT_NAME", nullable = false)
    private String tenantName;

    @Column(name = "CLASS_NAME", nullable = false)
    private String className;

    @Column(name = "MODEL_NAME", nullable = false)
    private String modelName;

    @Column(name = "AUDIT_TEXT", nullable = false, length = 2000)
    private String auditText;

    @Column(name = "EXTERNAL_TEXT", length = 2000)
    private String externalText;

    public AuditPayload() {
        this.generateAuditId();
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public AuditOperation getAuditOperation() {
        return auditOperation;
    }

    public void setAuditOperation(AuditOperation auditOperation) {
        this.auditOperation = auditOperation;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    @Override
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getAuditText() {
        return auditText;
    }

    public void setAuditText(String auditText) {
        this.auditText = auditText;
    }

    public String getExternalText() {
        return externalText;
    }

    public void setExternalText(String externalText) {
        this.externalText = externalText;
    }

    private void generateAuditId() {
        final int suffixLength = 50;
        final StringBuilder id = new StringBuilder();
        id.append(this.getDateTime()).append("-");
        for (int counter = 0; counter < suffixLength; counter++) {
            id.append((char) ('A' + ((int) (Math.random() * 26))));
        }
        setAuditId(id.toString());
    }
}