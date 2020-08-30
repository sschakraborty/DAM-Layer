package com.sschakraborty.platform.damlayer.audit.core.creator.resource;

import com.sschakraborty.platform.damlayer.audit.configuration.AuditConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuditResourceCreatorProvider {
    private static final Map<Class<? extends AuditResourceCreator>, AuditResourceCreator> CREATOR_MAP = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("unchecked")
    public static <T extends AuditResourceCreator> T getCreator(AuditConfiguration auditConfiguration, Class<T> creatorClass) {
        return (T) CREATOR_MAP.computeIfAbsent(creatorClass, key -> {
            try {
                return creatorClass.getConstructor(AuditConfiguration.class).newInstance(auditConfiguration);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        });
    }
}