/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Uladzislau Seuruk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    private final VariablePickingStrategy variablePickingStrategy;

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
