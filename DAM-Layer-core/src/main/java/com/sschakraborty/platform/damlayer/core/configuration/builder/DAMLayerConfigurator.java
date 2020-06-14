package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.core.GenericDAO;
import com.sschakraborty.platform.damlayer.core.GenericDAOImpl;
import com.sschakraborty.platform.damlayer.core.TenantDataServiceResolver;
import com.sschakraborty.platform.damlayer.core.cache.TenantDataServiceCache;
import com.sschakraborty.platform.damlayer.core.cache.TenantDataServiceMapCacheImpl;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadataBean;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilderImpl;
import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantServiceImpl;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a configurator in builder pattern for DAM Layer
 */
public class DAMLayerConfigurator {
    private ConnectorMetadata primaryConnectorMetadata;
    private List<Class<? extends Model>> classes;

    private ConfigurationBuilder configurationBuilder = new ConfigurationBuilderImpl();

    public final DAMLayerConfigurator withPrimaryConnectorMetadata(final ConnectorMetadata connectorMetadata) {
        this.primaryConnectorMetadata = connectorMetadata;
        return this;
    }

    @SafeVarargs
    public final DAMLayerConfigurator withAnnotatedModels(final Class<? extends Model>... classes) {
        if (this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.addAll(Arrays.asList(classes));
        return this;
    }

    public final DAMLayerConfigurator withAnnotatedModels(final List<Class<? extends Model>> classes) {
        if (this.classes == null) {
            this.classes = new ArrayList<>();
        }
        this.classes.addAll(classes);
        return this;
    }

    public GenericDAO build() throws Exception {
        if (this.primaryConnectorMetadata == null) {
            throw new Exception("Primary connector metadata has not been defined!");
        }
        if (this.classes == null) {
            throw new Exception("No models found. Need to register at least one annotated model class!");
        }

        final TenantService tenantService = buildTenantService(primaryConnectorMetadata);
        final TenantDataServiceCache tenantDataServiceCache = new TenantDataServiceMapCacheImpl();
        final TenantDataServiceResolver tenantDataServiceResolver = new TenantDataServiceResolver(
                tenantService, tenantDataServiceCache, configurationBuilder, classes
        );
        return new GenericDAOImpl(tenantService, tenantDataServiceResolver);
    }

    private TenantService buildTenantService(ConnectorMetadata connectorMetadata) {
        final Configuration configuration = configurationBuilder.build(
                connectorMetadata,
                Arrays.asList(
                        TenantConfigurationBean.class,
                        ConnectorMetadataBean.class
                )
        );
        return new TenantServiceImpl(BuilderUtil.buildTransactionManager(configuration));
    }
}