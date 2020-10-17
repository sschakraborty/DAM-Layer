package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;

public class TenantDetailsLRUCacheImpl implements TenantDetailsCache {
    private final Cache<String, TenantDetails> lruCache;

    public TenantDetailsLRUCacheImpl(int maxSize) {
        lruCache = new LRUCache<>(maxSize);
    }

    @Override
    public boolean exists(String tenantId) {
        return lruCache.exists(tenantId);
    }

    @Override
    public DataService getDataService(String tenantId) {
        return exists(tenantId) ? lruCache.get(tenantId).getDataService() : null;
    }

    @Override
    public TenantConfiguration getConfiguration(String tenantId) {
        return exists(tenantId) ? lruCache.get(tenantId).getTenantConfiguration() : null;
    }

    @Override
    public void put(String tenantId, TenantConfiguration tenantConfiguration, DataService dataService) {
        lruCache.put(tenantId, new TenantDetails(tenantConfiguration, dataService));
    }

    @Override
    public void refresh(String tenantId) {
        lruCache.invalidate(tenantId);
    }

    private static class TenantDetails {
        private final TenantConfiguration tenantConfiguration;
        private final DataService dataService;

        public TenantDetails(TenantConfiguration tenantConfiguration, DataService dataService) {
            this.tenantConfiguration = tenantConfiguration;
            this.dataService = dataService;
        }

        public TenantConfiguration getTenantConfiguration() {
            return tenantConfiguration;
        }

        public DataService getDataService() {
            return dataService;
        }
    }
}