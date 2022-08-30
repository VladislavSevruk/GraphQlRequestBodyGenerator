/*
 * MIT License
 *
 * Copyright (c) 2022 Uladzislau Seuruk
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
import com.github.vladislavsevruk.generator.util.GqlNamePicker;

/**
 * Provides variables values extraction logic by annotated value type.
 */
public class AnnotatedArgumentValueExtractor {

    /**
     * Returns default value for received variable annotation. Please note that string value should be additionally
     * escaped by double quotes, e.g. "1", "\"string value\"", "ENUM_VALUE".
     *
     * @param variableType <code>GqlVariableType</code> to extract value from.
     * @return <code>String</code> with default value.
     */
    public String getDefaultValue(GqlVariableType variableType) {
        return variableType.defaultValue();
    }

    /**
     * Returns variable name for received variable annotation.
     *
     * @param variableType <code>GqlVariableType</code> to extract value from.
     * @param fallback     <code>String</code> with value to use if extracted value is empty.
     * @return <code>String</code> with variable name.
     */
    public String getVariableName(GqlVariableType variableType, String fallback) {
        String nameFromAnnotation = variableType.variableName();
        return !nameFromAnnotation.isEmpty() ? nameFromAnnotation : fallback;
    }

    /**
     * Returns variable type name for received variable annotation.
     *
     * @param variableType <code>GqlVariableType</code> to extract value from.
     * @param value        <code>Object</code> with value to pick type for if extracted value is empty.
     * @return <code>String</code> with variable type name.
     */
    public String getVariableType(GqlVariableType variableType, Object value) {
        String typeNameFromAnnotation = variableType.variableType();
        return !typeNameFromAnnotation.isEmpty() ? typeNameFromAnnotation
                : GqlNamePicker.getGqlTypeName(value);
    }

    /**
     * Checks if received variable annotation represent argument should be marked as required.
     *
     * @param variableType <code>GqlVariableType</code> to extract value from.
     * @return <code>true</code> if received argument should be marked as required, <code>false</code> if not.
     */
    public boolean isRequired(GqlVariableType variableType) {
        return variableType.isRequired();
    }
}
