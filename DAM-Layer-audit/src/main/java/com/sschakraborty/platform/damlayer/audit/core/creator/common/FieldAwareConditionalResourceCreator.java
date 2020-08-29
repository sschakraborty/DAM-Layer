package com.sschakraborty.platform.damlayer.audit.core.creator.common;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.utility.AuditConstant;
import com.sschakraborty.platform.damlayer.audit.utility.AuditCrypto;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import io.vertx.core.json.Json;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldAwareConditionalResourceCreator {
    protected String createResource(Model model, AuditFieldConditionPredicate predicate) {
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
                        value = AuditConstant.MASK;
                    } else if (AuditField.Type.ENCRYPTED == auditFieldType) {
                        value = AuditCrypto.encrypt(field.get(model), AuditConstant.ENC_KEY);
                    } else {
                        value = field.get(model);
                    }
                    fieldMap.put(field.getName(), value);
                } catch (IllegalAccessException e) {
                    // TODO: Log if required
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return model.getModelName() + " " + Json.encode(fieldMap);
    }

    protected interface AuditFieldConditionPredicate {
        boolean calculate(AuditField auditField);
    }
}