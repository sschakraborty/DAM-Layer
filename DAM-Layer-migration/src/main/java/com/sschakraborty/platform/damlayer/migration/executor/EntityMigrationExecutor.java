package com.sschakraborty.platform.damlayer.migration.executor;

import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.migration.stage.entity.DestinationDataWriteStage;
import com.sschakraborty.platform.damlayer.migration.stage.entity.EntityTransformationStage;
import com.sschakraborty.platform.damlayer.migration.stage.entity.HasMoreEntityCheckStage;
import com.sschakraborty.platform.damlayer.migration.stage.entity.SourceDataReadStage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.Arrays;
import java.util.List;

public class EntityMigrationExecutor extends AbstractMigrationExecutor {
    public EntityMigrationExecutor(MigrationConfiguration migrationConfiguration, Entry currentEntry) {
        super(migrationConfiguration, currentEntry);
    }

    @Override
    protected List<Stage> getStageSequence() {
        return Arrays.asList(
                new SourceDataReadStage(),
                new EntityTransformationStage(),
                new DestinationDataWriteStage(),
                new HasMoreEntityCheckStage()
        );
    }
}
