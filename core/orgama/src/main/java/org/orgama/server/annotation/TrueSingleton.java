package org.orgama.server.annotation;
import com.google.inject.ScopeAnnotation;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Scope annotation that allows for an instance of a class to be bound as an
 * absolute singleton.  
 * @author kguthrie
 */
@Retention(RUNTIME) 
@ScopeAnnotation
public @interface TrueSingleton {}
