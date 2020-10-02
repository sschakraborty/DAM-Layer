package com.sschakraborty.platform.damlayer.migration;

import com.sschakraborty.platform.damlayer.core.DataManager;
import com.sschakraborty.platform.damlayer.transformation.Transformer;

public class MigrationConfiguration {
    private int maxThreadCount;
    private Transformer transformer;
    private DataManager dataManager;
    private String sourceTenantId;
    private String destinationTenantId;
    private int batchSize = 0;

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    public void setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public DataManager getGenericDAO() {
        return dataManager;
    }

    public void setGenericDAO(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public String getSourceTenantId() {
        return sourceTenantId;
    }

    public void setSourceTenantId(String sourceTenantId) {
        this.sourceTenantId = sourceTenantId;
    }

    public String getDestinationTenantId() {
        return destinationTenantId;
    }

    public void setDestinationTenantId(String destinationTenantId) {
        this.destinationTenantId = destinationTenantId;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}