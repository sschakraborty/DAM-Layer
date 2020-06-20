package com.sschakraborty.platform.damlayer.migration.executor;

import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContextImpl;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.List;

public abstract class AbstractMigrationExecutor implements Executor {
    private final List<Stage> stages = getStageSequence();
    private final MigrationContext migrationContext = new MigrationContextImpl();
    private final MigrationConfiguration migrationConfiguration;
    private final Entry currentEntry;

    public AbstractMigrationExecutor(MigrationConfiguration migrationConfiguration, Entry currentEntry) {
        this.migrationConfiguration = migrationConfiguration;
        this.currentEntry = currentEntry;
    }

    @Override
    public void run() {
        try {
            final DataService sourceDataService = getSourceDataService();
            final DataService destinationDataService = getDestinationDataService();
            sourceDataService.transactionManager().executeStateful((sourceUnit, sourceResult) -> {
                destinationDataService.transactionManager().executeStateful((destinationUnit, destinationResult) -> {
                    migrationContext.setSourceSession(sourceUnit.getSession());
                    migrationContext.setDestinationSession(destinationUnit.getSession());
                    if (migrationContext.shouldIterate()) {
                        stages.forEach(stage -> {
                            stage.runStage(migrationContext);
                        });
                    }
                });
            });
        } catch (Exception e) {
            // TODO: Log if required
        }
    }

    private DataService getDestinationDataService() throws Exception {
        final String destinationTenantId = migrationConfiguration.getDestinationTenantId();
        return migrationConfiguration.getGenericDAO().resolveDataService(destinationTenantId);
    }

    private DataService getSourceDataService() throws Exception {
        final String sourceTenantId = migrationConfiguration.getSourceTenantId();
        return migrationConfiguration.getGenericDAO().resolveDataService(sourceTenantId);
    }

    protected abstract List<Stage> getStageSequence();
}