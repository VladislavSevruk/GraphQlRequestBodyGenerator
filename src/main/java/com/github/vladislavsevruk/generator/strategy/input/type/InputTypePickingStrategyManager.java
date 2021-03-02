package com.github.vladislavsevruk.generator.strategy.input.type;

import lombok.Getter;

/**
 * Contains predefined {@link InputTypePickingStrategy} for input argument
 *
 * @see InputTypePickingStrategy
 * @see InputTypePickingStrategyImpl
 */
public enum InputTypePickingStrategyManager {

    INPUT_TYPE(new InputTypePickingStrategyImpl());

    @Getter
    private InputTypePickingStrategy inputTypePickingStrategy;

    InputTypePickingStrategyManager(InputTypePickingStrategy inputTypePickingStrategy) {
        this.inputTypePickingStrategy = inputTypePickingStrategy;
    }

    /**
     * Returns strategy for input type.
     *
     * @return <code>InputTypePickingStrategyManager</code> with input type strategy.
     */
    public static InputTypePickingStrategyManager withInputType() {
        return INPUT_TYPE;
    }

    /**
     * Returns default strategy for input type.
     *
     * @return <code>InputTypePickingStrategyManager</code> with default input type strategy.
     */
    public static InputTypePickingStrategyManager defaultStrategy() {
        return withInputType();
    }
}
