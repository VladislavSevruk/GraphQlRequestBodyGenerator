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
package com.github.vladislavsevruk.generator.param;

import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import lombok.Value;

/**
 * Represents input for GraphQL mutations.
 *
 * @param <T> type of value.
 */
@Value
public class GqlVariableArgument<T> implements GqlParameterValue<T> {

    String defaultValue;
    String name;
    boolean required;
    String type;
    T value;
    String variableName;

    private GqlVariableArgument(String name, String variableName, T value, String type, boolean required,
            String defaultValue) {
        this.name = name;
        this.variableName = variableName;
        this.value = value;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public static <U> GqlVariableArgument<U> of(String name, U value) {
        return of(name, name, value);
    }

    public static <U> GqlVariableArgument<U> of(String name, U value, boolean required) {
        return of(name, name, value, required);
    }

    public static <U> GqlVariableArgument<U> of(String name, String variableName, U value) {
        return of(name, variableName, value, false);
    }

    public static <U> GqlVariableArgument<U> of(String name, String variableName, U value, boolean required) {
        return of(name, variableName, value, GqlNamePicker.getGqlTypeName(value), required, null);
    }

    public static <U> GqlVariableArgument<U> of(String name, String variableName, U value, String type,
            boolean required) {
        return of(name, variableName, value, type, required, null);
    }

    public static <U> GqlVariableArgument<U> of(String name, String variableName, U value, String type,
            boolean required, String defaultValue) {
        return new GqlVariableArgument<>(name, variableName, value, type, required, defaultValue);
    }
}
