package com.github.vladislavsevruk.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks model class by Input type.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GqlInputType {

    /**
     * Returns inputType.
     *
     * @return <code>String</code> as input type.
     */
    String inputType();
}
