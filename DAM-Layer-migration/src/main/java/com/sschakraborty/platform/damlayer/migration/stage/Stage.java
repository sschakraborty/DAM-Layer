package com.sschakraborty.platform.damlayer.migration.stage;

import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

public interface Stage {
    <S extends Model, D extends Model> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    );
}