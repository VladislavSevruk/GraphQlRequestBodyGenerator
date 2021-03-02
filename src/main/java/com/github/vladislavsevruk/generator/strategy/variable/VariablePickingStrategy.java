/*
 * MIT License
 *
 * Copyright (c) 2021 Uladzislau Seuruk
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
