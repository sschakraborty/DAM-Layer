package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataServiceImpl;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class TenantDetailsLRUCacheImplTest {
    private static final int MAX_SIZE = 5;
    private static TenantDetailsLRUCacheImpl lruCache;

    @BeforeClass
    public static void init() {
        lruCache = new TenantDetailsLRUCacheImpl(MAX_SIZE);
    }

    @Test
    public void testPutAndFetch() {
        final String tenantId = "abracadabra";
        Assert.assertNull(lruCache.getDataService(tenantId));
        Assert.assertNull(lruCache.getConfiguration(tenantId));
        Assert.assertFalse(lruCache.exists(tenantId));
        final TenantConfiguration tenantConfiguration = new TenantConfiguration();
        final DataServiceImpl dataService = new DataServiceImpl(Mockito.mock(TransactionManager.class));
        lruCache.put(tenantId, tenantConfiguration, dataService);
        Assert.assertTrue(lruCache.exists(tenantId));
        Assert.assertEquals(lruCache.getConfiguration(tenantId), tenantConfiguration);
        Assert.assertEquals(lruCache.getDataService(tenantId), dataService);
        lruCache.refresh(tenantId);
        Assert.assertFalse(lruCache.exists(tenantId));
        Assert.assertNull(lruCache.getDataService(tenantId));
        Assert.assertNull(lruCache.getConfiguration(tenantId));
    }


    @Test
    public void testCapacity() {
        final String tenantId = "abracadabra";
        final TenantConfiguration tenantConfiguration = new TenantConfiguration();
        final DataServiceImpl dataService = new DataServiceImpl(Mockito.mock(TransactionManager.class));
        lruCache.put(tenantId, tenantConfiguration, dataService);
        Assert.assertTrue(lruCache.exists(tenantId));
        Assert.assertEquals(lruCache.getConfiguration(tenantId), tenantConfiguration);
        Assert.assertEquals(lruCache.getDataService(tenantId), dataService);

        // Put as many as maxSize - 1 number of elements
        for (int i = 0; i < MAX_SIZE - 1; i++) {
            lruCache.put(tenantId + i, new TenantConfiguration(), new DataServiceImpl(Mockito.mock(TransactionManager.class)));
        }
        Assert.assertTrue(lruCache.exists(tenantId));
        Assert.assertEquals(lruCache.getConfiguration(tenantId), tenantConfiguration);
        Assert.assertEquals(lruCache.getDataService(tenantId), dataService);

        lruCache.put(tenantId + MAX_SIZE, new TenantConfiguration(), new DataServiceImpl(Mockito.mock(TransactionManager.class)));
        Assert.assertTrue(lruCache.exists(tenantId));
        Assert.assertEquals(lruCache.getConfiguration(tenantId), tenantConfiguration);
        Assert.assertEquals(lruCache.getDataService(tenantId), dataService);
    }

    @Test
    public void testCapacityMaxedOut() {
        final String tenantId = "abracadabra";
        final TenantConfiguration tenantConfiguration = new TenantConfiguration();
        final DataServiceImpl dataService = new DataServiceImpl(Mockito.mock(TransactionManager.class));
        lruCache.put(tenantId, tenantConfiguration, dataService);
        Assert.assertTrue(lruCache.exists(tenantId));
        Assert.assertEquals(lruCache.getConfiguration(tenantId), tenantConfiguration);
        Assert.assertEquals(lruCache.getDataService(tenantId), dataService);

        // Put as many as maxSize number of elements
        for (int i = 0; i <= MAX_SIZE; i++) {
            lruCache.put(tenantId + i, new TenantConfiguration(), new DataServiceImpl(Mockito.mock(TransactionManager.class)));
        }
        Assert.assertFalse(lruCache.exists(tenantId));
    }
}