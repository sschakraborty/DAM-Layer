package com.sschakraborty.platform.damlayer.core.service;

import com.sschakraborty.platform.damlayer.core.marker.Model;

import java.util.Arrays;
import java.util.List;

public interface DataManipulationService {
    default void insert(Model... models) {
        this.insert(Arrays.asList(models));
    }

    default void update(Model... models) {
        this.update(Arrays.asList(models));
    }

    default void save(Model... models) {
        this.save(Arrays.asList(models));
    }

    default void delete(Model... models) {
        this.delete(Arrays.asList(models));
    }

    void insert(List<Model> models);

    void update(List<Model> models);

    void save(List<Model> models);

    void delete(List<Model> models);
}