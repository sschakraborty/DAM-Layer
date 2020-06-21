package com.sschakraborty.platform.damlayer.migration.stage.minibatch;

import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

public class FinalizeStage implements Stage {
    @Override
    public <S extends Model, D extends Model> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    ) {
        final int nextFetchOffset = migrationContext.getFetchOffset() + migrationContext.getSourceObjects().size();
        migrationContext.setShouldIterate(!migrationContext.getDestinationObjects().isEmpty());
        migrationContext.setFetchOffset(nextFetchOffset);
        migrationContext.setSourceObjects(null);
        migrationContext.setDestinationObjects(null);
    }
}