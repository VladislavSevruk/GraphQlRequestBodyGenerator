package com.github.vladislavsevruk.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks model which value should be treated as operation variable provided it was met at operation
 * arguments.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GqlVariableType {

    /**
     * Returns variable type name for annotated type. Please note that string value should be additionally escaped by
     * double quotes, e.g. "1", "\"string value\"", "ENUM_VALUE".
     */
    String defaultValue() default "";

    /**
     * Return <code>true</code> if annotated type should be marked as required, <code>false</code> if not.
     */
    boolean isRequired() default false;

    /**
     * Returns variable type name for annotated type.
     */
    String variableName() default "";

    /**
     * Returns variable type name for annotated type.
     */
    String variableType() default "";
}
