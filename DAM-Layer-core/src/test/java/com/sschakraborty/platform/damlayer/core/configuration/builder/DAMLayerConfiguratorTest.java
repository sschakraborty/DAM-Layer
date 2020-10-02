package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.core.DataManager;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadataBean;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.configuration.builder.model.Item;
import com.sschakraborty.platform.damlayer.core.configuration.builder.model.Parcel;
import com.sschakraborty.platform.damlayer.core.service.DataManipulationService;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.core.service.QueryService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DAMLayerConfiguratorTest {
    private static final boolean RUN_PERFORMANCE_TESTS = false;

    @Test
    public void testMultiTenancy() throws Exception {
        ConnectorMetadata primaryConnectorMetadata = getPrimaryConnectorMetadata();
        DAMLayerConfigurator configurator = new DAMLayerConfigurator();
        configurator.withPrimaryConnectorMetadata(primaryConnectorMetadata);
        DataManager dataManager = configurator.build();

        Parcel parcel = createDummyData();

        final TenantConfiguration tenantConfiguration = getTenantConfiguration();

        try {
            dataManager.resolveConfiguration(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            dataManager.resolveDataService(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        dataManager.registerTenant(tenantConfiguration);

        TenantConfiguration fetchedConfig = dataManager.resolveConfiguration(tenantConfiguration.getId());
        Assert.assertNotNull(fetchedConfig);
        Assert.assertEquals(tenantConfiguration.getName(), fetchedConfig.getName());

        DataService dataService = dataManager.resolveDataService(tenantConfiguration.getId());
        Assert.assertNotNull(dataService);

        fetchedConfig = dataManager.resolveConfiguration(tenantConfiguration.getId());
        Assert.assertNotNull(fetchedConfig);

        testDataOperationsWithDummyDataAndDataService(dataService, parcel);
        if (RUN_PERFORMANCE_TESTS) {
            testPerformance(dataService);
        }

        dataManager.unregisterTenant(tenantConfiguration.getId());

        try {
            dataManager.resolveConfiguration(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            dataManager.resolveDataService(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    private void testDataOperationsWithDummyDataAndDataService(DataService dataService, Parcel parcel) {
        DataManipulationService dataManipulationService = dataService.getDataManipulationService();
        QueryService queryService = dataService.getQueryService();

        dataManipulationService.insert(parcel);

        Parcel fetchedParcel = queryService.fetchTree(Parcel.class, parcel.getId());
        Parcel fetchedParcelTree = queryService.fetchTree(Parcel.class, parcel.getId());
        Assert.assertEquals(3, fetchedParcelTree.getItems().size());

        dataManipulationService.delete(fetchedParcel);

        fetchedParcel = queryService.fetchTree(Parcel.class, parcel.getId());
        Assert.assertNull(fetchedParcel);
    }

    private void testPerformance(DataService dataService) {
        final int queryCount = 2000;
        final long expectedMilliseconds = 5000;

        DataManipulationService dataManipulationService = dataService.getDataManipulationService();
        QueryService queryService = dataService.getQueryService();

        Parcel parcel = createDummyData();
        dataManipulationService.insert(parcel);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < queryCount; i++) {
            Parcel fetchedParcelTree = queryService.fetchTree(Parcel.class, parcel.getId());
            Assert.assertEquals(2, fetchedParcelTree.getItems().size());
        }
        long endTime = System.currentTimeMillis();
        long requiredMilliseconds = endTime - startTime;

        Parcel fetchedParcel = queryService.fetch(Parcel.class, parcel.getId());
        dataManipulationService.delete(fetchedParcel);

        System.out.printf("Time taken for %d tree queries: %d ms", queryCount, requiredMilliseconds);
        if (requiredMilliseconds > expectedMilliseconds) {
            Assert.fail();
        }
    }

    private Parcel createDummyData() {
        Parcel parcel = new Parcel();
        parcel.setFromAddress("India");
        parcel.setToAddress("Canada");

        Item item1 = new Item();
        item1.setName("Item_1");
        item1.setParcel(parcel);

        Item item2 = new Item();
        item2.setName("Item_2");
        item2.setParcel(parcel);

        Item item3 = new Item();
        item3.setName("Item_2");
        item3.setParcel(parcel);

        parcel.setItems(Arrays.asList(item1, item2));
        parcel.setPrimaryItem(item3);
        return parcel;
    }

    private ConnectorMetadata getPrimaryConnectorMetadata() {
        final ConnectorMetadataBean primary = ConnectorMetadata.createBean();
        primary.setUsername("root");
        primary.setPassword("admin");
        primary.setDialectClass("org.hibernate.dialect.MySQL57Dialect");
        primary.setDriverClass("com.mysql.cj.jdbc.Driver");
        primary.setConnectionURLPrefix("jdbc:mysql:");
        primary.setConnectionHost("localhost");
        primary.setConnectionPort(3306);
        primary.setDatabaseName("DAMLayer_Metadata");
        primary.setDdlMode("create-drop");
        primary.setMaxPoolSize(5);
        return primary;
    }

    private TenantConfiguration getTenantConfiguration() {
        final ConnectorMetadataBean conf = ConnectorMetadata.createBean();
        conf.setUsername("root");
        conf.setPassword("admin");
        conf.setDialectClass("org.hibernate.dialect.MySQL57Dialect");
        conf.setDriverClass("com.mysql.cj.jdbc.Driver");
        conf.setConnectionURLPrefix("jdbc:mysql:");
        conf.setConnectionHost("localhost");
        conf.setConnectionPort(3306);
        conf.setDatabaseName("DAMLayer_TooToo");
        conf.setDdlMode("create-drop");
        conf.setMaxPoolSize(150);
        conf.setIdleTimeout(1500);
        conf.setConnectionTimeout(1500);

        TenantConfigurationBean tenantConfiguration = TenantConfiguration.createBean();
        tenantConfiguration.setName("Test_tenant");
        tenantConfiguration.setId("too_too");
        tenantConfiguration.setConnectorMetadata(conf);
        tenantConfiguration.setAnnotatedClasses(Arrays.asList(
                Parcel.class,
                Item.class
        ));

        return tenantConfiguration;
    }
}