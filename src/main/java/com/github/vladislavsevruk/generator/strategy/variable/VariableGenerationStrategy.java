package com.github.vladislavsevruk.generator.strategy.variable;

import lombok.Getter;

/**
 * Contains predefined {@link VariablePickingStrategy} for variables detection:<ul>
 * <li> detect by argument type
 * <li> detect by annotation at argument value type
 * </ul>
 *
 * @see VariablePickingStrategy
 * @see VariableArgumentTypeStrategy
 * @see AnnotatedArgumentValueTypeStrategy
 */
public enum VariableGenerationStrategy {

    ANNOTATED_ARGUMENT_VALUE_TYPE(new AnnotatedArgumentValueTypeStrategy()),
    BY_ARGUMENT_TYPE(new VariableArgumentTypeStrategy());

    @Getter
    private VariablePickingStrategy variablePickingStrategy;

    VariableGenerationStrategy(VariablePickingStrategy variablePickingStrategy) {
        this.variablePickingStrategy = variablePickingStrategy;
    }

    /**
     * Returns strategy for detecting variables by argument type.
     */
    public static VariableGenerationStrategy annotatedArgumentValueType() {
        return ANNOTATED_ARGUMENT_VALUE_TYPE;
    }

    /**
     * Returns strategy for detecting variables by argument type.
     */
    public static VariableGenerationStrategy byArgumentType() {
        return BY_ARGUMENT_TYPE;
    }

    /**
     * Returns default variables detection strategy.
     */
    public static VariableGenerationStrategy defaultStrategy() {
        return byArgumentType();
    }
}
