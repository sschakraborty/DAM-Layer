package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.core.GenericDAO;
import com.sschakraborty.platform.damlayer.core.GenericDAOImpl;
import com.sschakraborty.platform.damlayer.core.TenantDetailsResolver;
import com.sschakraborty.platform.damlayer.core.audit.AuditPayload;
import com.sschakraborty.platform.damlayer.core.audit.auditor.Auditor;
import com.sschakraborty.platform.damlayer.core.audit.auditor.DefaultAuditor;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsCache;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsMapCacheImpl;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadataBean;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilderImpl;
import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantServiceImpl;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManagerImpl;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a configurator in builder pattern for DAM Layer
 */
public class DAMLayerConfigurator {
    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilderImpl();
    private ConnectorMetadata primaryConnectorMetadata;
    private List<Class<? extends Model>> classes;
    private Auditor auditor = null;

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

    public final DAMLayerConfigurator withAuditor(Auditor auditor) {
        this.auditor = auditor;
        return this;
    }

    public GenericDAO build() throws Exception {
        if (this.primaryConnectorMetadata == null) {
            throw new Exception("Primary connector metadata has not been defined!");
        }
        if (this.classes == null) {
            throw new Exception("No models found. Need to register at least one annotated model class!");
        }

        final TenantService tenantService = buildTenantServiceAndPopulateAuditor(primaryConnectorMetadata);
        final TenantDetailsCache tenantDetailsCache = new TenantDetailsMapCacheImpl();
        final TenantDetailsResolver tenantDetailsResolver = new TenantDetailsResolver(
                tenantService, tenantDetailsCache, configurationBuilder, classes, auditor
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
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(
                configuration,
                tenantConfiguration,
                null
        );
        return transactionManager;
    }
}