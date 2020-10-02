package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.service.DataService;

public interface DataManager {
    String TENANT_ID_NULL_ERROR = "Tenant configuration or tenant ID cannot be null!";

    void saveTenant(TenantConfiguration tenantConfiguration) throws Exception;

    default void deleteTenant(TenantConfiguration tenantConfiguration) throws Exception {
        if (tenantConfiguration != null && tenantConfiguration.getId() != null) {
            deleteTenant(tenantConfiguration.getId());
        } else {
            throw new Exception(TENANT_ID_NULL_ERROR);
        }
    }

    void deleteTenant(String tenantId) throws Exception;

    TenantConfiguration getTenant(String tenantId) throws Exception;

    default DataService getDataService(TenantConfiguration tenantConfiguration) throws Exception {
        if (tenantConfiguration != null && tenantConfiguration.getId() != null) {
            return getDataService(tenantConfiguration.getId());
        }
        throw new Exception(TENANT_ID_NULL_ERROR);
    }

    DataService getDataService(String tenantId) throws Exception;
}