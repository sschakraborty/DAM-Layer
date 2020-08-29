package com.sschakraborty.platform.damlayer.audit.annotation;

import com.sschakraborty.platform.damlayer.audit.core.creator.remark.AuditRemarkCreator;
import com.sschakraborty.platform.damlayer.audit.core.creator.remark.DefaultAuditRemarkCreator;
import com.sschakraborty.platform.damlayer.audit.core.creator.resource.AuditResourceCreator;
import com.sschakraborty.platform.damlayer.audit.core.creator.resource.DefaultAuditResourceCreator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AuditResource {
    /**
     * @return a flag which indicates whether auditing is disabled for this particular resource
     */
    boolean enabled() default true;

    /**
     * @return Returns the audit resource creator class
     */
    Class<? extends AuditResourceCreator> resourceCreator() default DefaultAuditResourceCreator.class;

    /**
     * @return Returns the audit remark creator class
     */
    Class<? extends AuditRemarkCreator> remarkCreator() default DefaultAuditRemarkCreator.class;
}