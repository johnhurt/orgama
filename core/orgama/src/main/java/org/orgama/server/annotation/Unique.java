package org.orgama.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for fields within Objectify Entities that indicates the field is 
 * unique.  This is not a performant operation, so use only where necessary
 * @author kguthrie
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface Unique {}
