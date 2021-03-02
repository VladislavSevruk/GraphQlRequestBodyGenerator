package com.github.vladislavsevruk.generator.strategy.variable;

import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;

/**
 * Provides variables detection strategy by annotated value type of argument.
 */
public class AnnotatedArgumentValueTypeStrategy implements VariablePickingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(GqlParameterValue<?> argument) {
        return getAnnotation(argument).defaultValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariableName(GqlParameterValue<?> argument) {
        String nameFromAnnotation = getAnnotation(argument).variableName();
        return !nameFromAnnotation.isEmpty() ? nameFromAnnotation : argument.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariableType(GqlParameterValue<?> argument) {
        String typeNameFromAnnotation = getAnnotation(argument).variableType();
        return !typeNameFromAnnotation.isEmpty() ? typeNameFromAnnotation
                : GqlNamePicker.getGqlTypeName(argument.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequired(GqlParameterValue<?> argument) {
        return getAnnotation(argument).isRequired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVariable(GqlParameterValue<?> argument) {
        return getAnnotation(argument) != null;
    }

    private GqlVariableType getAnnotation(GqlParameterValue<?> argument) {
        return argument.getValue().getClass().getAnnotation(GqlVariableType.class);
    }
}
