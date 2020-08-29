package com.sschakraborty.platform.damlayer.audit.core.creator.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AuditResourceCreatorProvider {
    private static final Map<Class<? extends AuditResourceCreator>, AuditResourceCreator> CREATOR_MAP = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends AuditResourceCreator> T getCreator(Class<T> creatorClass) {
        return (T) CREATOR_MAP.computeIfAbsent(creatorClass, key -> {
            try {
                return creatorClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        });
    }
}