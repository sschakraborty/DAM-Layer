package com.sschakraborty.platform.damlayer.core.service;

import java.util.Arrays;
import java.util.List;

public interface DataManipulationService {
    default void insert(Object... models) {
        this.insert("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void update(Object... models) {
        this.update("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void save(Object... models) {
        this.save("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void delete(Object... models) {
        this.delete("Operation performed by Default-SYS-USER", Arrays.asList(models));
    }

    default void insert(String externalText, Object... models) {
        this.insert(externalText, Arrays.asList(models));
    }

    default void update(String externalText, Object... models) {
        this.update(externalText, Arrays.asList(models));
    }

    default void save(String externalText, Object... models) {
        this.save(externalText, Arrays.asList(models));
    }

    default void delete(String externalText, Object... models) {
        this.delete(externalText, Arrays.asList(models));
    }

    void insert(String externalText, List<Object> models);

    void update(String externalText, List<Object> models);

    void save(String externalText, List<Object> models);

    void delete(String externalText, List<Object> models);
}