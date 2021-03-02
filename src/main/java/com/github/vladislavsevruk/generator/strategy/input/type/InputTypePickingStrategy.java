package com.github.vladislavsevruk.generator.strategy.input.type;

import com.github.vladislavsevruk.generator.param.GqlParameterValue;

/**
 * Marks DTO model picking strategy to provide input type.
 */
@FunctionalInterface
public interface InputTypePickingStrategy {

    /**
     * Returns input type.
     *
     * @param argument <code>GqlParameterValue</code> varargs with argument names and values.
     * @return         <code>String</code> with input type.
     */
    String getInputType(GqlParameterValue<?> argument);
}
