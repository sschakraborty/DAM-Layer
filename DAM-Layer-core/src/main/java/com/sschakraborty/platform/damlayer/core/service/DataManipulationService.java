package com.sschakraborty.platform.damlayer.core.service;

import java.util.Arrays;
import java.util.List;

public interface DataManipulationService {
    default void insert(Model... models) {
        this.insert("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void update(Model... models) {
        this.update("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void save(Model... models) {
        this.save("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void delete(Model... models) {
        this.delete("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void insert(String externalText, Model... models) {
        this.insert(externalText, Arrays.asList(models));
    }

    default void update(String externalText, Model... models) {
        this.update(externalText, Arrays.asList(models));
    }

    default void save(String externalText, Model... models) {
        this.save(externalText, Arrays.asList(models));
    }

    default void delete(String externalText, Model... models) {
        this.delete(externalText, Arrays.asList(models));
    }

    void insert(String externalText, List<Model> models);

    void update(String externalText, List<Model> models);

    void save(String externalText, List<Model> models);

    void delete(String externalText, List<Model> models);
}