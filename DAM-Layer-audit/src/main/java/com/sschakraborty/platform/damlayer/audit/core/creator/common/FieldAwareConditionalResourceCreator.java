package com.sschakraborty.platform.damlayer.audit.core.creator.common;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.audit.utility.AuditCrypto;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FieldAwareConditionalResourceCreator {
    private static final Predicate<Field> IDENTIFIER_MAKING_PREDICATE = (field -> field.getAnnotation(AuditField.class).identifier());
    private static final Predicate<Field> RESOURCE_MAKING_PREDICATE = (field -> true);
    private final byte[] cryptoKey;
    private final String secretMask;

    public FieldAwareConditionalResourceCreator(byte[] cryptoKey, String secretMask) {
        this.cryptoKey = cryptoKey;
        this.secretMask = secretMask;
    }

    protected Object createIdentifier(Object model) {
        return createResource(model, IDENTIFIER_MAKING_PREDICATE);
    }

    protected Object createResource(Object model) {
        return createResource(model, RESOURCE_MAKING_PREDICATE);
    }

    private boolean isAuditEnabled(Object object) {
        final AuditResource auditResource = object.getClass().getAnnotation(AuditResource.class);
        return auditResource != null && auditResource.enabled();
    }

    private List<Field> getAllAuditEnabledFields(Object model) {
        return Arrays.stream(model.getClass().getDeclaredFields()).filter(field -> {
            final AuditField auditField = field.getAnnotation(AuditField.class);
            return auditField != null && auditField.enabled();
        }).collect(Collectors.toList());
    }

    private Object processFieldValue(Object fieldValue) {
        if (fieldValue == null) return null;
        if (fieldValue instanceof Map) {
            final Map<?, ?> valueMap = (Map<?, ?>) fieldValue;
            final Map<Object, Object> returnMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : valueMap.entrySet()) {
                returnMap.put(entry.getKey(), processFieldValue(entry.getValue()));
            }
            return returnMap;
        }
        if (fieldValue instanceof Collection) {
            return ((Collection<?>) fieldValue).stream().map(this::processFieldValue).collect(Collectors.toList());
        }
        if (fieldValue instanceof Object[]) {
            return Arrays.stream((Object[]) fieldValue).map(this::processFieldValue).collect(Collectors.toList());
        }
        if (isAuditEnabled(fieldValue)) {
            return createIdentifier(fieldValue);
        } else {
            return fieldValue;
        }
    }

    private Map<String, Object> createResource(Object model, Predicate<Field> predicate) {
        final Map<String, Object> fieldMap = new HashMap<>();
        if (isAuditEnabled(model)) {
            final List<Field> allAuditEnabledFields = getAllAuditEnabledFields(model);
            final List<Field> selectedFields = allAuditEnabledFields.stream().filter(predicate).collect(Collectors.toList());
            for (Field field : selectedFields) {
                AuditField auditField = field.getAnnotation(AuditField.class);
                final String fieldName = field.getName();
                Object fieldValue = null;
                if (AuditField.Type.SECRET == auditField.fieldType()) {
                    fieldValue = secretMask;
                } else {
                    try {
                        field.setAccessible(true);
                        fieldValue = processFieldValue(field.get(model));
                        if (AuditField.Type.ENCRYPTED == auditField.fieldType() && fieldValue != null) {
                            fieldValue = AuditCrypto.encrypt(fieldValue, cryptoKey);
                        }
                    } catch (IllegalAccessException ignored) {
                    } finally {
                        field.setAccessible(false);
                    }
                }
                if (fieldValue != null) {
                    fieldMap.put(fieldName, fieldValue);
                }
            }
        }
        return fieldMap;
    }
}