package com.sschakraborty.platform.damlayer.audit.core.creator.identifier;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import io.vertx.core.json.Json;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class IdentifierCreator {
    public static String getIdentifier(Model model) {
        final Map<String, Object> introspection = new LinkedHashMap<>();
        final Field[] fields = model.getClass().getFields();
        for (final Field field : fields) {
            final AuditField auditField = field.getAnnotation(AuditField.class);
            if (auditField != null && auditField.identifier()) {
                field.setAccessible(true);
                try {
                    introspection.put(field.getName(), field.get(model));
                } catch (IllegalAccessException e) {
                    // TODO: Log if required
                }
                field.setAccessible(false);
            }
        }
        return model.getModelName() + " " + Json.encode(introspection);
    }
}