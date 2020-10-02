package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

import java.util.List;

public class MigrationContextImpl implements MigrationContext {
    private SessionWrapper sourceSession, destinationSession;
    private boolean shouldIterate = true;
    private int fetchOffset = 0;
    private List<?> sourceObjects, destinationObjects;

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

    @Override
    public int getFetchOffset() {
        return this.fetchOffset;
    }

    @Override
    public void setFetchOffset(int offset) {
        this.fetchOffset = offset;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> List<S> getSourceObjects() {
        return (List<S>) sourceObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> void setSourceObjects(List<S> sourceObjects) {
        this.sourceObjects = sourceObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> List<D> getDestinationObjects() {
        return (List<D>) this.destinationObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D> void setDestinationObjects(List<D> destinationObjects) {
        this.destinationObjects = destinationObjects;
    }
}