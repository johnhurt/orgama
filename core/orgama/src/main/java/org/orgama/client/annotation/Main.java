// <copyright file="Main.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.client.annotation;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;


/**
 *
 * @author kguthrie
 */
@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface Main {}