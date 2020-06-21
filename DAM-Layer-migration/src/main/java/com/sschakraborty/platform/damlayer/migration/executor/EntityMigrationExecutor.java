package com.sschakraborty.platform.damlayer.migration.executor;

import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.migration.stage.minibatch.DestinationDataWriteStage;
import com.sschakraborty.platform.damlayer.migration.stage.minibatch.EntityTransformationStage;
import com.sschakraborty.platform.damlayer.migration.stage.minibatch.FinalizeStage;
import com.sschakraborty.platform.damlayer.migration.stage.minibatch.SourceDataReadStage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.Arrays;
import java.util.List;

public class EntityMigrationExecutor<S extends Model, D extends Model> extends AbstractMigrationExecutor<S, D> {
    private final int batchSize;

    public EntityMigrationExecutor(MigrationConfiguration migrationConfiguration, Entry<S, D> currentEntry) {
        super(migrationConfiguration, currentEntry);
        this.batchSize = migrationConfiguration.getBatchSize();
    }

    @Override
    protected List<Stage> getStageSequence() {
        return Arrays.asList(
                new SourceDataReadStage(batchSize),
                new EntityTransformationStage(),
                new DestinationDataWriteStage(),
                new FinalizeStage()
        );
    }
}
