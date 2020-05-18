/*
 * MIT License
 *
 * Copyright (c) 2020 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.generator.mutation;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import com.github.vladislavsevruk.generator.util.StreamUtil;
import com.github.vladislavsevruk.generator.util.StringUtil;
import com.github.vladislavsevruk.resolver.util.PrimitiveWrapperUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates body for GraphQL mutations with received arguments and selection set according to different field picking
 * strategies.
 */
public class GqlMutationBodyGenerator {

    private static final String DELIMITER = ",";
    private static final String GETTER_PREFIX = "get";
    private static final Logger logger = LogManager.getLogger(GqlMutationBodyGenerator.class);
    private final FieldMarkingStrategy inputFieldMarkingStrategy;
    private final String mutationName;
    private final SelectionSetGenerator selectionSetGenerator;

    public GqlMutationBodyGenerator(String mutationName, SelectionSetGenerator selectionSetGenerator) {
        this(mutationName, selectionSetGenerator, FieldMarkingStrategySourceManager.input().getStrategy());
    }

    public GqlMutationBodyGenerator(String mutationName, SelectionSetGenerator selectionSetGenerator,
            FieldMarkingStrategy inputFieldMarkingStrategy) {
        Objects.requireNonNull(inputFieldMarkingStrategy);
        Objects.requireNonNull(selectionSetGenerator);
        this.inputFieldMarkingStrategy = inputFieldMarkingStrategy;
        this.selectionSetGenerator = selectionSetGenerator;
        this.mutationName = mutationName;
    }

    /**
     * Builds GraphQL mutation body with received arguments according to received field picking strategies.
     *
     * @param inputFieldsPickingStrategy        <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                          mutation input.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for
     *                                          mutation selection set.
     * @param arguments                         <code>GqlParameterValue</code> varargs with argument names and values.
     * @return <code>String</code> with resulted GraphQL mutation.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            FieldsPickingStrategy selectionSetFieldsPickingStrategy, GqlParameterValue<?>... arguments) {
        return generate(inputFieldsPickingStrategy, selectionSetFieldsPickingStrategy, Arrays.asList(arguments));
    }

    /**
     * Builds GraphQL mutation body with received arguments according to received field picking strategies.
     *
     * @param inputFieldsPickingStrategy        <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                          mutation input.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for
     *                                          mutation selection set.
     * @param arguments                         <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                          names and values.
     * @return <code>String</code> with resulted GraphQL mutation.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        Objects.requireNonNull(arguments);
        logger.info(() -> String.format("Generating '%s' GraphQL mutation.", mutationName));
        String argumentsStr = generateGqlArguments(inputFieldsPickingStrategy, arguments);
        String selectionSet = selectionSetGenerator.generate(selectionSetFieldsPickingStrategy);
        String mutation = "mutation{" + mutationName + argumentsStr + selectionSet + "}";
        logger.debug(() -> "Resulted mutation: " + mutation);
        return mutation;
    }

    private boolean canBeGetter(Method method) {
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        Class<?> returnType = method.getReturnType();
        return !(returnType.equals(void.class) || returnType.equals(Void.class));
    }

    private void collectValue(String fieldName, String fieldValue,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        logger.debug(() -> String.format("Received '%s' value for '%s' input field.", fieldValue, fieldName));
        if (inputFieldsPickingStrategy.shouldBePicked(fieldName, fieldValue)) {
            logger.debug(() -> String.format("Picked '%s' input field.", fieldName));
            mutationValues.put(fieldName, fieldValue);
        }
    }

    private void collectValuesByFields(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        for (Field field : valueClass.getDeclaredFields()) {
            if (field.isSynthetic() || !inputFieldMarkingStrategy.isMarkedField(field)) {
                continue;
            }
            logger.debug(() -> String.format("Marked '%s' input field.", field.getName()));
            if (field.getAnnotation(GqlDelegate.class) != null) {
                logger.debug(() -> String.format("'%s' is delegate.", field.getName()));
                Object delegateObject = generateArgumentValue(field, value);
                collectValuesForDelegate(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else {
                logger.debug(() -> String.format("'%s' is input field.", field.getName()));
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
            GqlInput fieldAnnotation = method.getAnnotation(GqlInput.class);
            GqlDelegate delegateAnnotation = method.getAnnotation(GqlDelegate.class);
            if (fieldAnnotation == null && delegateAnnotation == null) {
                continue;
            }
            logger.debug(() -> String.format("Marked '%s' input method.", method.getName()));
            if (delegateAnnotation != null) {
                logger.debug(() -> String.format("'%s' is delegate.", method.getName()));
                Object delegateObject = generateArgumentValueByMethod(value, method);
                collectValuesForDelegate(delegateObject, inputFieldsPickingStrategy, mutationValues);
            } else {
                logger.debug(() -> String.format("'%s' is input method.", method.getName()));
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
            logger.debug(() -> String.format("Input field '%s' is already collected.", fieldName));
        } else {
            String argumentValue = generateInputArgumentValue(generateArgumentValue(field, value),
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
                logger.debug(() -> String.format("Input field '%s' is already collected.", entryKey));
            } else {
                String entryValue = generateInputArgumentValue(entry.getValue(), inputFieldsPickingStrategy);
                collectValue(entryKey, entryValue, inputFieldsPickingStrategy, mutationValues);
            }
        }
        return mutationValues;
    }

    private void collectValuesForMethod(Object value, Method method,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        String methodName = method.getAnnotation(GqlInput.class).name();
        if (!mutationValues.containsKey(methodName)) {
            String argumentValue = generateInputArgumentValue(generateArgumentValueByMethod(value, method),
                    inputFieldsPickingStrategy);
            collectValue(methodName, argumentValue, inputFieldsPickingStrategy, mutationValues);
        }
    }

    private LinkedHashMap<String, String> collectValuesForModel(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        logger.debug(() -> "Generating mutation value for " + valueClass.getName());
        collectValuesByFields(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        collectValuesByMethods(value, valueClass, inputFieldsPickingStrategy, mutationValues);
        return mutationValues;
    }

    private LinkedHashMap<String, String> collectValuesMap(Object value, Class<?> valueClass,
            InputFieldsPickingStrategy inputFieldsPickingStrategy, LinkedHashMap<String, String> mutationValues) {
        if (Map.class.isAssignableFrom(valueClass)) {
            logger.debug(() -> valueClass.getName() + " is map.");
            return collectValuesForMap(value, inputFieldsPickingStrategy, mutationValues);
        }
        return collectValuesForModel(value, valueClass, inputFieldsPickingStrategy, mutationValues);
    }

    private List<String> convertToStringList(Object value, InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        return StreamUtil.createStream(value).map(item -> generateInputArgumentValue(item, inputFieldsPickingStrategy))
                .collect(Collectors.toList());
    }

    private Method findGetterMethod(Field field) {
        for (Method method : field.getDeclaringClass().getMethods()) {
            if (method.getAnnotation(GqlIgnore.class) != null || !canBeGetter(method)) {
                continue;
            }
            String nameFromAnnotation = getNameFromAnnotation(method);
            if (nameFromAnnotation.equals(GqlNamePicker.getFieldName(field))) {
                return method;
            }
            String methodName = method.getName();
            String clearedMethodName = removeGetterPrefixIfPresent(methodName);
            if (methodName.equals(field.getName()) || clearedMethodName.equals(field.getName())) {
                return method;
            }
        }
        return null;
    }

    private Object generateArgumentValue(Field field, Object value) {
        Method getterMethod = findGetterMethod(field);
        if (getterMethod != null) {
            logger.debug(() -> String
                    .format("Found '%s' getter method for '%s' field.", getterMethod.getName(), field.getName()));
            return generateArgumentValueByMethod(value, getterMethod);
        } else {
            logger.debug(() -> String.format("There was no getter method for '%s' field found.", field.getName()));
            return generateArgumentValueByField(value, field);
        }
    }

    private String generateArgumentValue(GqlParameterValue<?> argument,
            InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        String argumentName = argument.getName();
        if (!"input".equals(argumentName)) {
            return argument.getName() + ":" + StringUtil.generateEscapedValueString(argument.getValue());
        }
        Object inputValue = argument.getValue();
        logger.debug(() -> String.format("Generating input argument value for '%s' model using '%s' input field "
                        + "marking strategy and '%s' field picking strategy.", inputValue.getClass().getName(),
                inputFieldMarkingStrategy.getClass().getName(), inputFieldsPickingStrategy.getClass().getName()));
        return argument.getName() + ":" + generateInputArgumentValue(inputValue, inputFieldsPickingStrategy);
    }

    private Object generateArgumentValueByField(Object value, Field field) {
        boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(value);
        } catch (ReflectiveOperationException roEx) {
            logger.error(() -> String.format("Failed to get value of '%s' field.", field.getName()), roEx);
            return null;
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    private Object generateArgumentValueByMethod(Object value, Method getterMethod) {
        try {
            return getterMethod.invoke(value);
        } catch (ReflectiveOperationException roEx) {
            logger.error(() -> String.format("Failed to execute '%s' getter method.", getterMethod.getName()), roEx);
            return null;
        }
    }

    private String generateGqlArguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        if (!arguments.iterator().hasNext()) {
            logger.warn("GraphQL mutation argument iterable is empty.");
            return "";
        }
        return "(" + StreamSupport.stream(arguments.spliterator(), false)
                .map(argument -> generateArgumentValue(argument, inputFieldsPickingStrategy))
                .collect(Collectors.joining(",")) + ")";
    }

    private String generateInputArgumentValue(Object value, InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        if (Objects.isNull(value)) {
            logger.debug("Value is null.");
            return "null";
        }
        Class<?> valueClass = value.getClass();
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            logger.debug(() -> valueClass.getName() + " is char sequence.");
            // add escaped quotes for literals
            return StringUtil.addQuotesForStringArgument(value.toString());
        }
        if (isSimpleType(valueClass)) {
            logger.debug(() -> valueClass.getName() + " is simple type.");
            return String.valueOf(value);
        }
        if (Iterable.class.isAssignableFrom(valueClass) || valueClass.isArray()) {
            logger.debug(() -> valueClass.getName() + " is iterable or array.");
            // compose all elements through the comma and surround by square brackets
            return "[" + String.join(",", convertToStringList(value, inputFieldsPickingStrategy)) + "]";
        }
        LinkedHashMap<String, String> modelValues = collectValuesMap(value, value.getClass(),
                inputFieldsPickingStrategy, new LinkedHashMap<>());
        String argumentValue = modelValues.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(DELIMITER));
        return "{" + argumentValue + "}";
    }

    private String getNameFromAnnotation(Method method) {
        GqlInput fieldAnnotation = method.getAnnotation(GqlInput.class);
        return fieldAnnotation != null ? fieldAnnotation.name() : "";
    }

    private boolean isSimpleType(Class<?> valueClass) {
        return valueClass.isPrimitive() || PrimitiveWrapperUtil.isWrapper(valueClass) || valueClass.isEnum()
                || Number.class.isAssignableFrom(valueClass) || Date.class.isAssignableFrom(valueClass);
    }

    private String removeGetterPrefixIfPresent(String methodName) {
        if (methodName.startsWith(GETTER_PREFIX)) {
            int prefixLength = GETTER_PREFIX.length();
            return methodName.substring(prefixLength, prefixLength + 1).toLowerCase() + methodName
                    .substring(prefixLength + 1);
        }
        return methodName;
    }
}
