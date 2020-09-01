package com.sschakraborty.platform.damlayer.audit.core.creator.common;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.audit.utility.AuditCrypto;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldAwareConditionalResourceCreator {
    public static final AuditFieldConditionPredicate IDENTIFIER_MAKING_PREDICATE = AuditField::identifier;
    private final byte[] cryptoKey;
    private final String secretMask;

    public FieldAwareConditionalResourceCreator(byte[] cryptoKey, String secretMask) {
        this.cryptoKey = cryptoKey;
        this.secretMask = secretMask;
    }

    protected Map<String, Object> createResource(Model model, AuditFieldConditionPredicate predicate) {
        final Map<String, Object> fieldMap = new LinkedHashMap<>();
        final Field[] fields = model.getClass().getDeclaredFields();
        for (final Field field : fields) {
            final AuditField auditField = field.getAnnotation(AuditField.class);
            if (auditField != null && auditField.enabled() && predicate.calculate(auditField)) {
                field.setAccessible(true);
                try {
                    final AuditField.Type auditFieldType = auditField.fieldType();
                    Object value;
                    if (AuditField.Type.SECRET == auditFieldType) {
                        value = secretMask;
                    } else if (AuditField.Type.ENCRYPTED == auditFieldType) {
                        value = processEncryptedField(model, field);
                    } else {
                        value = processAuditEnabledModels(field.get(model));
                    }
                    if (value != null) {
                        fieldMap.put(field.getName(), value);
                    }
                } catch (IllegalAccessException ignored) {
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return fieldMap;
    }

    private Object processEncryptedField(Model model, Field field) throws IllegalAccessException {
        Object value;
        value = processAuditEnabledModels(field.get(model));
        if (value != null) {
            value = AuditCrypto.encrypt(value, cryptoKey);
        }
        return value;
    }

    private Object processAuditEnabledModels(Object value) {
        if (value == null) return null;
        if (value instanceof Collection) {
            final Collection<?> collection = (Collection<?>) value;
            return collection.stream().map(this::processObject).collect(Collectors.toList());
        }
        return processObject(value);
    }

    private Object processObject(Object value) {
        final AuditResource auditResource = value.getClass().getAnnotation(AuditResource.class);
        if (value instanceof Model && auditResource != null) {
            if (auditResource.enabled()) {
                final Model model = (Model) value;
                return createResource(model, IDENTIFIER_MAKING_PREDICATE);
            } else {
                return null;
            }
        } else {
            return value;
        }
    }

    protected interface AuditFieldConditionPredicate {
        boolean calculate(AuditField auditField);
    }
}