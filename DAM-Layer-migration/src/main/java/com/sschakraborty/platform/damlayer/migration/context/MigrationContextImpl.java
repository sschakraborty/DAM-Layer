package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.util.List;

public class MigrationContextImpl implements MigrationContext {
    private SessionWrapper sourceSession, destinationSession;
    private boolean shouldIterate = true;
    private int fetchOffset = 0;
    private List<Model> sourceObjects, destinationObjects;

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
    public <S extends Model> List<S> getSourceObjects() {
        return (List<S>) sourceObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends Model> void setSourceObjects(List<S> sourceObjects) {
        this.sourceObjects = (List<Model>) sourceObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D extends Model> List<D> getDestinationObjects() {
        return (List<D>) this.destinationObjects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <D extends Model> void setDestinationObjects(List<D> destinationObjects) {
        this.destinationObjects = (List<Model>) destinationObjects;
    }
}