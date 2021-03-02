package com.github.vladislavsevruk.generator.strategy.variable;

import com.github.vladislavsevruk.generator.param.GqlParameterValue;

/**
 * Provides variables detection strategy.
 */
public interface VariablePickingStrategy {

    /**
     * Returns default value for received variable. Please note that string value should be additionally escaped by
     * double quotes, e.g. "1", "\"string value\"", "ENUM_VALUE".
     *
     * @param argument <code>GqlParameterValue</code> that was used for operation.
     * @return <code>String</code> with default value.
     */
    String getDefaultValue(GqlParameterValue<?> argument);

    /**
     * Returns variable name for received variable.
     *
     * @param argument <code>GqlParameterValue</code> that was used for operation.
     * @return <code>String</code> with variable name.
     */
    String getVariableName(GqlParameterValue<?> argument);

    /**
     * Returns variable type name for received variable.
     *
     * @param argument <code>GqlParameterValue</code> that was used for operation.
     * @return <code>String</code> with variable type name.
     */
    String getVariableType(GqlParameterValue<?> argument);

    /**
     * Checks if received argument should be marked as required.
     *
     * @param argument <code>GqlParameterValue</code> that was used for operation.
     * @return <code>true</code> if received argument should be marked as required, <code>false</code> if not.
     */
    boolean isRequired(GqlParameterValue<?> argument);

    /**
     * Checks if received argument should be treated as variable.
     *
     * @param argument <code>GqlParameterValue</code> that was used for operation.
     * @return <code>true</code> if received argument should be treated as variable, <code>false</code> if it should be
     * treated as argument.
     */
    boolean isVariable(GqlParameterValue<?> argument);
}
