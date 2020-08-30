package com.sschakraborty.platform.damlayer.audit.core.creator.remark;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuditRemarkCreatorProvider {
    private static final Map<Class<? extends AuditRemarkCreator>, AuditRemarkCreator> CREATOR_MAP = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("unchecked")
    public static <T extends AuditRemarkCreator> T getCreator(Class<T> creatorClass) {
        return (T) CREATOR_MAP.computeIfAbsent(creatorClass, key -> {
            try {
                return creatorClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        });
    }
}