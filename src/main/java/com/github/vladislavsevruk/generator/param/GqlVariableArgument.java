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
