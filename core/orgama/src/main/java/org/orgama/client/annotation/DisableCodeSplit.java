package org.orgama.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation indicating that the annotated presenter should not use 
 * ProxyCodeSplit
 * @author kguthrie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableCodeSplit {}
