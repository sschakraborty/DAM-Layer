package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

public interface MigrationContext {
    SessionWrapper getSourceSession();

    void setSourceSession(SessionWrapper sessionWrapper);

    SessionWrapper getDestinationSession();

    void setDestinationSession(SessionWrapper sessionWrapper);

    boolean shouldIterate();

    void setShouldIterate(boolean shouldIterate);
}