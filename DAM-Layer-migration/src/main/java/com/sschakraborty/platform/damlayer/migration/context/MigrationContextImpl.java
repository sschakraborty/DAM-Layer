package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

public class MigrationContextImpl implements MigrationContext {
    private SessionWrapper sourceSession, destinationSession;
    private boolean shouldIterate = true;

    @Override
    public SessionWrapper getSourceSession() {
        return sourceSession;
    }

    @Override
    public void setSourceSession(SessionWrapper sourceSession) {
        this.sourceSession = sourceSession;
    }

    @Override
    public SessionWrapper getDestinationSession() {
        return destinationSession;
    }

    @Override
    public void setDestinationSession(SessionWrapper destinationSession) {
        this.destinationSession = destinationSession;
    }

    @Override
    public boolean shouldIterate() {
        return this.shouldIterate;
    }

    @Override
    public void setShouldIterate(boolean shouldIterate) {
        this.shouldIterate = shouldIterate;
    }
}