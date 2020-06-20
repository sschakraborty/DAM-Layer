package com.sschakraborty.platform.damlayer.migration.stage;

import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;

public interface Stage {
    void runStage(MigrationContext migrationContext);
}