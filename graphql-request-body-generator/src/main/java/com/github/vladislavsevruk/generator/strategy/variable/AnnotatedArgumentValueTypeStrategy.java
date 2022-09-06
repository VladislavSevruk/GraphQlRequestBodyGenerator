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

import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;

/**
 * Provides variables detection strategy by annotated value type of argument.
 */
public class AnnotatedArgumentValueTypeStrategy implements VariablePickingStrategy {

    private final AnnotatedArgumentValueExtractor valueExtractor = new AnnotatedArgumentValueExtractor();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue(GqlParameterValue<?> argument) {
        return valueExtractor.getDefaultValue(getAnnotation(argument));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariableName(GqlParameterValue<?> argument) {
        return valueExtractor.getVariableName(getAnnotation(argument), argument.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariableType(GqlParameterValue<?> argument) {
        return valueExtractor.getVariableType(getAnnotation(argument), argument.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequired(GqlParameterValue<?> argument) {
        return valueExtractor.isRequired(getAnnotation(argument));
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
