package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.core.GenericDAO;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadataBean;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import org.junit.Assert;
import org.junit.Test;

public class DAMLayerConfiguratorTest {
    @Test
    public void testMultiTenancy() throws Exception {
        ConnectorMetadata primaryConnectorMetadata = getPrimaryConnectorMetadata();
        DAMLayerConfigurator configurator = new DAMLayerConfigurator();
        configurator.withPrimaryConnectorMetadata(primaryConnectorMetadata);
        GenericDAO genericDAO = configurator.withAnnotatedModels().build();

        final TenantConfiguration tenantConfiguration = getTenantConfiguration();

        try {
            genericDAO.resolveConfiguration(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            genericDAO.resolveDataService(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        genericDAO.registerTenant(tenantConfiguration);

        TenantConfiguration fetchedConfig = genericDAO.resolveConfiguration(tenantConfiguration.getId());
        Assert.assertNotNull(fetchedConfig);
        Assert.assertEquals(tenantConfiguration.getName(), fetchedConfig.getName());

        DataService dataService = genericDAO.resolveDataService(tenantConfiguration.getId());
        Assert.assertNotNull(dataService);

        fetchedConfig = genericDAO.resolveConfiguration(tenantConfiguration.getId());
        Assert.assertNotNull(fetchedConfig);

        genericDAO.unregisterTenant(tenantConfiguration.getId());

        try {
            genericDAO.resolveConfiguration(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            genericDAO.resolveDataService(tenantConfiguration.getId());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
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

        TenantConfigurationBean tenantConfiguration = TenantConfiguration.createBean();
        tenantConfiguration.setName("Test_tenant");
        tenantConfiguration.setId("too_too");
        tenantConfiguration.setConnectorMetadata(conf);

        return tenantConfiguration;
    }
}