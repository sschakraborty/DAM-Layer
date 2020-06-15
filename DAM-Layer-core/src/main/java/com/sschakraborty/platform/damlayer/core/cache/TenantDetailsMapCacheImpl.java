package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;

import java.util.HashMap;
import java.util.Map;

public class TenantDetailsMapCacheImpl implements TenantDetailsCache {
    private final Map<String, TenantDetails> map = new HashMap<>();

    @Override
    public boolean exists(String tenantId) {
        return map.containsKey(tenantId);
    }

    @Override
    public DataService getDataService(String tenantId) {
        return exists(tenantId) ? map.get(tenantId).getDataService() : null;
    }

    @Override
    public TenantConfiguration getConfiguration(String tenantId) {
        return exists(tenantId) ? map.get(tenantId).getTenantConfiguration() : null;
    }

    @Override
    public void put(String tenantId, TenantConfiguration tenantConfiguration, DataService dataService) {
        map.put(tenantId, new TenantDetails(tenantConfiguration, dataService));
    }

    @Override
    public void refresh(String tenantId) {
        map.remove(tenantId);
    }

    private static class TenantDetails {
        private TenantConfiguration tenantConfiguration;
        private DataService dataService;

        public TenantDetails(TenantConfiguration tenantConfiguration, DataService dataService) {
            this.tenantConfiguration = tenantConfiguration;
            this.dataService = dataService;
        }

        public TenantConfiguration getTenantConfiguration() {
            return tenantConfiguration;
        }

        public void setTenantConfiguration(TenantConfiguration tenantConfiguration) {
            this.tenantConfiguration = tenantConfiguration;
        }

        public DataService getDataService() {
            return dataService;
        }

        public void setDataService(DataService dataService) {
            this.dataService = dataService;
        }
    }
}
