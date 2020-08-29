package com.sschakraborty.platform.damlayer.audit.annotation;

import com.sschakraborty.platform.damlayer.audit.core.creator.AuditResourceCreator;
import com.sschakraborty.platform.damlayer.audit.core.creator.DefaultAuditResourceCreator;

public @interface AuditResource {
    /**
     * @return a flag which indicates whether auditing is disabled for this particular resource
     */
    boolean disableAuditing() default false;

    /**
     * @return Returns the audit resource creator class
     */
    Class<? extends AuditResourceCreator> value() default DefaultAuditResourceCreator.class;
}