package com.sschakraborty.platform.damlayer.audit.annotation;

import com.sschakraborty.platform.damlayer.audit.core.AuditResourceCreator;

public @interface AuditResource {
    /**
     * @return Name for the audit resource
     */
    String value() default "Unnamed Resource";

    /**
     * @return Creator class
     */
    Class<? extends AuditResourceCreator> resourceCreatorClass();
}