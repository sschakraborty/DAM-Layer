package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.service.DataService;

import java.util.HashMap;
import java.util.Map;

public class TenantDataServiceMapCacheImpl implements TenantDataServiceCache {
    private final Map<String, DataService> map = new HashMap<>();

    @Override
    public boolean exists(String tenantId) {
        return map.containsKey(tenantId);
    }

    @Override
    public DataService get(String tenantId) {
        return map.get(tenantId);
    }

    @Override
    public void put(String tenantId, DataService dataService) {
        map.put(tenantId, dataService);
    }
}
