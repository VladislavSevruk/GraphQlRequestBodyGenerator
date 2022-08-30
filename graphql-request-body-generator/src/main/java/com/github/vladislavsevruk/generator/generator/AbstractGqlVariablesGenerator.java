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
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.AnnotatedArgumentValueExtractor;
import com.github.vladislavsevruk.generator.util.ArgumentValueUtil;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * GraphQL variables generator with common logic for GraphQL operation.
 */
@Log4j2
public abstract class AbstractGqlVariablesGenerator {

    protected final AnnotatedArgumentValueExtractor annotatedArgumentValueExtractor
            = new AnnotatedArgumentValueExtractor();
    protected final FieldMarkingStrategy inputFieldMarkingStrategy;

    protected AbstractGqlVariablesGenerator(FieldMarkingStrategy inputFieldMarkingStrategy) {
        this.inputFieldMarkingStrategy = inputFieldMarkingStrategy;
    }

    protected Map<String, Object> collectDelegatedValuesMap(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Object delegate, Class<?> delegateClass, Map<String, Object> operationVariables) {
        if (Map.class.isAssignableFrom(delegateClass)) {
            log.debug("{} is map.", delegateClass.getName());
            return Collections.emptyMap();
        }
        return collectDelegatedValuesForModel(delegate, delegateClass, inputFieldsPickingStrategy, operationVariables);
    }

    protected abstract Object getVariableValue(Object value, Field field, GqlVariableType variableType);

    protected abstract Object getVariableValue(Object value, Method method, GqlVariableType variableType);

    private void collectDelegatedValues(Object delegateObject, InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Map<String, Object> mutationValues) {
        if (delegateObject != null) {
            collectDelegatedValuesMap(inputFieldsPickingStrategy, delegateObject, delegateObject.getClass(),
                    mutationValues);
        }
    }

    private void collectDelegatedValuesByFields(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, Map<String, Object> mutationValues) {
        for (Field field : valueClass.getDeclaredFields()) {
            if (field.isSynthetic() || !inputFieldMarkingStrategy.isMarkedField(field)) {
                continue;
            }
            log.debug("Marked '{}' input field.", field.getName());
            if (field.getAnnotation(GqlDelegate.class) != null) {
                log.debug("'{}' is delegate.", field.getName());
                Object delegateObject = ArgumentValueUtil.getValue(field, value);
                collectDelegatedValues(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else if (field.getAnnotation(GqlVariableType.class) != null) {
                log.debug("'{}' is variable.", field.getName());
                String fieldName = GqlNamePicker.getFieldName(field);
                GqlVariableType variableType = field.getAnnotation(GqlVariableType.class);
                String variableName = annotatedArgumentValueExtractor.getVariableName(variableType, fieldName);
                Object variableValue = getVariableValue(value, field, variableType);
                collectValue(variableName, variableValue, inputFieldsPickingStrategy, mutationValues);
            }
        }
        Class<?> superclass = valueClass.getSuperclass();
        if (superclass != null && !Object.class.equals(superclass)) {
            collectDelegatedValuesByFields(value, superclass, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private void collectDelegatedValuesByMethods(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, Map<String, Object> mutationValues) {
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
                collectDelegatedValues(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else if (method.getAnnotation(GqlVariableType.class) != null) {
                log.debug("'{}' is variable.", method.getName());
                collectDelegatedValuesForMethodVariable(value, method, inputFieldsPickingStrategy, mutationValues);
            }
        }
    }

    private void collectDelegatedValuesForMethodVariable(Object value, Method method,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, Map<String, Object> mutationValues) {
        String methodName = GqlNamePicker.getInputName(method);
        GqlVariableType variableTypeAnnotation = method.getAnnotation(GqlVariableType.class);
        String variableName = annotatedArgumentValueExtractor.getVariableName(variableTypeAnnotation, methodName);
        if (!mutationValues.containsKey(variableName)) {
            Object variableValue = getVariableValue(value, method, variableTypeAnnotation);
            collectValue(variableName, variableValue, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private Map<String, Object> collectDelegatedValuesForModel(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, Map<String, Object> mutationValues) {
        log.debug("Generating delegated variable values for {}", valueClass.getName());
        collectDelegatedValuesByFields(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        collectDelegatedValuesByMethods(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        return mutationValues;
    }

    private void collectValue(String fieldName, Object fieldValue,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, Map<String, Object> mutationValues) {
        log.debug("Received '{}' value for '{}' input field.", fieldValue, fieldName);
        if (inputFieldsPickingStrategy.shouldBePicked(fieldName, fieldValue)) {
            log.debug("Picked '{}' input field.", fieldName);
            mutationValues.put(fieldName, fieldValue);
        }
    }
}
