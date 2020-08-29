package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.core.GenericDAO;
import com.sschakraborty.platform.damlayer.core.GenericDAOImpl;
import com.sschakraborty.platform.damlayer.core.TenantDetailsResolver;
import com.sschakraborty.platform.damlayer.core.audit.DefaultAuditor;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsCache;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsMapCacheImpl;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadataBean;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilderImpl;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantServiceImpl;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManagerImpl;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

/**
 * Implementation of a configurator in builder pattern for DAM Layer
 */
public class DAMLayerConfigurator {
    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilderImpl();
    private ConnectorMetadata primaryConnectorMetadata;
    private int cacheSize;
    private Auditor auditor = null;

    public final DAMLayerConfigurator withPrimaryConnectorMetadata(final ConnectorMetadata connectorMetadata) {
        this.primaryConnectorMetadata = connectorMetadata;
        return this;
    }

    public final DAMLayerConfigurator withAuditor(Auditor auditor) {
        this.auditor = auditor;
        return this;
    }

    public final DAMLayerConfigurator withCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public GenericDAO build() throws Exception {
        if (this.primaryConnectorMetadata == null) {
            throw new Exception("Primary connector metadata has not been defined!");
        }
        final TenantService tenantService = buildTenantServiceAndPopulateAuditor(primaryConnectorMetadata);
        final TenantDetailsCache tenantDetailsCache = new TenantDetailsMapCacheImpl(cacheSize);
        final TenantDetailsResolver tenantDetailsResolver = new TenantDetailsResolver(
                tenantService, tenantDetailsCache, configurationBuilder, auditor
        );
        return new GenericDAOImpl(tenantService, tenantDetailsResolver);
    }

    private TenantService buildTenantServiceAndPopulateAuditor(ConnectorMetadata connectorMetadata) {
        final TransactionManager transactionManager = buildTenantTransactionManager(connectorMetadata);
        {
            this.auditor = (this.auditor == null) ? new DefaultAuditor(transactionManager) : this.auditor;
            ((TransactionManagerImpl) transactionManager).setAuditor(this.auditor);
        }
        return new TenantServiceImpl(transactionManager);
    }

    private TransactionManager buildTenantTransactionManager(ConnectorMetadata connectorMetadata) {
        final Configuration configuration = configurationBuilder.build(
                connectorMetadata,
                Arrays.asList(
                        TenantConfigurationBean.class,
                        ConnectorMetadataBean.class,
                        AuditPayload.class
                )
        );
        final TenantConfigurationBean tenantConfiguration = TenantConfiguration.createBean();
        tenantConfiguration.setId("DAMLayer-SYSTEM-USER");
        tenantConfiguration.setName("DAM-Layer-System-User-TENANT");
        tenantConfiguration.setConnectorMetadata((ConnectorMetadataBean) connectorMetadata);
        return BuilderUtil.buildTransactionManager(configuration, tenantConfiguration, null);
    }
}