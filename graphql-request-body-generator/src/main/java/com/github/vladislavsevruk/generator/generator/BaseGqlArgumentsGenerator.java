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
package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import com.github.vladislavsevruk.generator.param.GqlDelegateArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.AnnotatedArgumentValueExtractor;
import com.github.vladislavsevruk.generator.util.ArgumentValueUtil;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import com.github.vladislavsevruk.generator.util.StreamUtil;
import com.github.vladislavsevruk.generator.util.StringUtil;
import com.github.vladislavsevruk.resolver.util.PrimitiveWrapperUtil;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * GraphQL arguments generator with common logic for GraphQL operation.
 */
@Log4j2
public class BaseGqlArgumentsGenerator {

    private static final String DELIMITER = ",";

    protected final AnnotatedArgumentValueExtractor annotatedArgumentValueExtractor
            = new AnnotatedArgumentValueExtractor();
    protected final FieldMarkingStrategy inputFieldMarkingStrategy;

    protected BaseGqlArgumentsGenerator(FieldMarkingStrategy inputFieldMarkingStrategy) {
        Objects.requireNonNull(inputFieldMarkingStrategy);
        this.inputFieldMarkingStrategy = inputFieldMarkingStrategy;
    }

    protected String generateArgumentModelValue(Object value, InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        if (Objects.isNull(value)) {
            log.debug("Value is null.");
            return null;
        }
        Class<?> valueClass = value.getClass();
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            log.debug("{} is char sequence.", valueClass.getName());
            // add escaped quotes for literals
            return StringUtil.addQuotesForStringArgument(value.toString());
        }
        if (isSimpleType(valueClass)) {
            log.debug("{} is simple type.", valueClass.getName());
            return String.valueOf(value);
        }
        if (Iterable.class.isAssignableFrom(valueClass) || valueClass.isArray()) {
            log.debug("{} is iterable or array.", valueClass.getName());
            // compose all elements through the comma and surround by square brackets
            return "[" + String.join(",", convertToStringList(value, inputFieldsPickingStrategy)) + "]";
        }
        return "{" + generateModelArguments(value, inputFieldsPickingStrategy) + "}";
    }

    protected String generateModelArguments(Object value, InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        LinkedHashMap<String, String> modelValues = collectValuesMap(value, value.getClass(),
                inputFieldsPickingStrategy, new LinkedHashMap<>());
        return modelValues.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(DELIMITER));
    }

    protected boolean isDelegate(GqlParameterValue<?> argument) {
        return GqlDelegateArgument.class.equals(argument.getClass());
    }

    private void collectValue(String fieldName, String fieldValue,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        log.debug("Received '{}' value for '{}' input field.", fieldValue, fieldName);
        if (inputFieldsPickingStrategy.shouldBePicked(fieldName, fieldValue)) {
            log.debug("Picked '{}' input field.", fieldName);
            mutationValues.put(fieldName, fieldValue);
        }
    }

    private void collectValuesByFields(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        for (Field field : valueClass.getDeclaredFields()) {
            if (field.isSynthetic() || !inputFieldMarkingStrategy.isMarkedField(field)) {
                continue;
            }
            log.debug("Marked '{}' input field.", field.getName());
            if (field.getAnnotation(GqlDelegate.class) != null) {
                log.debug("'{}' is delegate.", field.getName());
                Object delegateObject = ArgumentValueUtil.getValue(field, value);
                collectValuesForDelegate(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else if (field.getAnnotation(GqlVariableType.class) != null) {
                log.debug("'{}' is variable.", field.getName());
                String fieldName = GqlNamePicker.getFieldName(field);
                GqlVariableType variableType = field.getAnnotation(GqlVariableType.class);
                String variableValue = "$" + annotatedArgumentValueExtractor.getVariableName(variableType, fieldName);
                collectValue(fieldName, variableValue, inputFieldsPickingStrategy, mutationValues);
            } else {
                log.debug("'{}' is input field.", field.getName());
                collectValuesForField(value, field, inputFieldsPickingStrategy, mutationValues);
            }
        }
        Class<?> superclass = valueClass.getSuperclass();
        if (superclass != null && !Object.class.equals(superclass)) {
            collectValuesByFields(value, superclass, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private void collectValuesByMethods(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        for (Method method : valueClass.getMethods()) {
            GqlInput inputAnnotation = method.getAnnotation(GqlInput.class);
            GqlDelegate delegateAnnotation = method.getAnnotation(GqlDelegate.class);
            if (inputAnnotation == null && delegateAnnotation == null) {
                continue;
            }
            log.debug("Marked '{}' input method.", method.getName());
            if (delegateAnnotation != null) {
                log.debug("'{}' is delegate.", method.getName());
                Object delegateObject = ArgumentValueUtil.getValueByMethod(value, method);
                collectValuesForDelegate(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else if (method.getAnnotation(GqlVariableType.class) != null) {
                log.debug("'{}' is variable.", method.getName());
                collectValuesForMethodVariable(method, inputFieldsPickingStrategy, mutationValues);
            } else {
                log.debug("'{}' is input method.", method.getName());
                collectValuesForMethod(value, method, inputFieldsPickingStrategy, mutationValues);
            }
        }
    }

    private void collectValuesForDelegate(Object delegateObject, InputFieldsPickingStrategy inputFieldsPickingStrategy,
            LinkedHashMap<String, String> mutationValues) {
        if (delegateObject != null) {
            collectValuesMap(delegateObject, delegateObject.getClass(), inputFieldsPickingStrategy, mutationValues);
        }
    }

    private void collectValuesForField(Object value, Field field, InputFieldsPickingStrategy inputFieldsPickingStrategy,
            LinkedHashMap<String, String> mutationValues) {
        String fieldName = GqlNamePicker.getFieldName(field);
        if (mutationValues.containsKey(fieldName)) {
            log.debug("Input field '{}' is already collected.", fieldName);
        } else {
            String argumentValue = generateArgumentModelValue(ArgumentValueUtil.getValue(field, value),
                    inputFieldsPickingStrategy);
            collectValue(fieldName, argumentValue, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private LinkedHashMap<String, String> collectValuesForMap(Object value,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        for (Object object : ((Map) value).entrySet()) {
            Entry<?, ?> entry = (Entry<?, ?>) object;
            String entryKey = entry.getKey().toString();
            if (mutationValues.containsKey(entryKey)) {
                log.debug("Input field '{}' is already collected.", entryKey);
            } else {
                String entryValue = generateArgumentModelValue(entry.getValue(), inputFieldsPickingStrategy);
                collectValue(entryKey, entryValue, inputFieldsPickingStrategy, mutationValues);
            }
        }
        return mutationValues;
    }

    private void collectValuesForMethod(Object value, Method method,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        String methodName = GqlNamePicker.getInputName(method);
        if (!mutationValues.containsKey(methodName)) {
            String argumentValue = generateArgumentModelValue(ArgumentValueUtil.getValueByMethod(value, method),
                    inputFieldsPickingStrategy);
            collectValue(methodName, argumentValue, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private void collectValuesForMethodVariable(Method method, InputFieldsPickingStrategy inputFieldsPickingStrategy,
            LinkedHashMap<String, String> mutationValues) {
        String methodName = GqlNamePicker.getInputName(method);
        GqlVariableType variableTypeAnnotation = method.getAnnotation(GqlVariableType.class);
        String variableValue = "$" + annotatedArgumentValueExtractor.getVariableName(variableTypeAnnotation,
                methodName);
        if (!mutationValues.containsKey(methodName)) {
            collectValue(methodName, variableValue, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private LinkedHashMap<String, String> collectValuesForModel(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        log.debug("Generating mutation value for {}", valueClass.getName());
        collectValuesByFields(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        collectValuesByMethods(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        return mutationValues;
    }

    private LinkedHashMap<String, String> collectValuesMap(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        if (Map.class.isAssignableFrom(valueClass)) {
            log.debug("{} is map.", valueClass.getName());
            return collectValuesForMap(value, inputFieldsPickingStrategy, mutationValues);
        }
        return collectValuesForModel(value, valueClass, inputFieldsPickingStrategy, mutationValues);
    }

    private List<String> convertToStringList(Object value, InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        return StreamUtil.createStream(value).map(item -> generateArgumentModelValue(item, inputFieldsPickingStrategy))
                .collect(Collectors.toList());
    }

    private boolean isSimpleType(Class<?> valueClass) {
        return valueClass.isPrimitive() || PrimitiveWrapperUtil.isWrapper(valueClass) || valueClass.isEnum()
                || Number.class.isAssignableFrom(valueClass) || Date.class.isAssignableFrom(valueClass);
    }
}
