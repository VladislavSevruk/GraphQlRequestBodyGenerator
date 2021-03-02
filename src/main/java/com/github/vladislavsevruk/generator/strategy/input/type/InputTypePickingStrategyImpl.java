package com.github.vladislavsevruk.generator.strategy.input.type;

import com.github.vladislavsevruk.generator.annotation.GqlInputType;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;

/**
 * Provides input type.
 */
public class InputTypePickingStrategyImpl implements InputTypePickingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInputType(GqlParameterValue<?> argument) {
        Class<?> classArg = argument.getValue().getClass();
        GqlInputType inputTypeAnnotation = classArg.getAnnotation(GqlInputType.class);
        return inputTypeAnnotation != null ? inputTypeAnnotation.inputType() : null;
    }
}
