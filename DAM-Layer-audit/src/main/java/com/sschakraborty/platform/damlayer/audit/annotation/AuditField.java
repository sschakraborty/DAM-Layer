package com.sschakraborty.platform.damlayer.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AuditField {
    /**
     * @return a flag indicating whether to audit this field or not
     */
    boolean enabled() default true;

    /**
     * @return Indicates the field type for the field that needs to be audited
     */
    Type fieldType() default Type.PLAIN_TEXT;

    /**
     * @return returns whether the audit field is an identifier
     */
    boolean identifier() default false;

    enum Type {
        /**
         * PLAIN_TEXT    refers to the audit where field value will be left as it is
         * SECRET        refers to the audit where field value will be irreversibly masked
         * ENCRYPTED     refers to the audit where field value will be symmetrically encrypted
         */
        PLAIN_TEXT,
        SECRET,
        ENCRYPTED
    }
}