/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.util;

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlFieldArgument;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import com.github.vladislavsevruk.generator.annotation.GqlUnionType;
import com.github.vladislavsevruk.resolver.util.PrimitiveWrapperUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Picks name for GraphQL items.
 */
public final class GqlNamePicker {

    private GqlNamePicker() {
    }

    /**
     * Gets field name for GraphQL operation from {@link GqlField} annotation if present or using field name.
     *
     * @param field <code>Field</code> to get name for.
     * @return <code>String</code> with field name.
     */
    public static String getFieldName(Field field) {
        GqlField fieldAnnotation = field.getAnnotation(GqlField.class);
        if (fieldAnnotation != null && !fieldAnnotation.name().isEmpty()) {
            return fieldAnnotation.name();
        }
        return field.getName();
    }

    /**
     * Gets name for GraphQL operation from {@link GqlField} annotation with field alias and arguments if present or
     * using field name.
     *
     * @param field <code>Field</code> to get name, alias and arguments for.
     * @return <code>String</code> with field name, alias and arguments.
     */
    public static String getFieldNameWithArgumentsAndAlias(Field field) {
        String fieldName = getFieldName(field);
        GqlField fieldAnnotation = field.getAnnotation(GqlField.class);
        if (fieldAnnotation != null) {
            fieldName = addAliasIfPresent(fieldName, fieldAnnotation);
            fieldName = addArgumentsIfPresent(fieldName, fieldAnnotation);
        }
        return fieldName;
    }

    /**
     * Gets suitable name for GraphQL primitives or uses class name if type is not primitive.
     *
     * @param value value to get GraphQL type name for.
     * @return <code>String</code> with GraphQL type name.
     */
    public static String getGqlTypeName(Object value) {
        Class<?> clazz = PrimitiveWrapperUtil.wrap(value.getClass());
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return "String";
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return "Boolean";
        }
        if (Long.class.equals(clazz) || Integer.class.equals(clazz) || Short.class.equals(clazz) || Byte.class
                .equals(clazz) || BigInteger.class.equals(clazz)) {
            return "Int";
        }
        if (Double.class.equals(clazz) || Float.class.equals(clazz) || BigDecimal.class.equals(clazz)) {
            return "Float";
        }
        return value.getClass().getSimpleName();
    }

    /**
     * Gets input name for GraphQL operation from {@link GqlInput} annotation if present or using field name.
     *
     * @param method <code>Method</code> to get name for.
     * @return <code>String</code> with input method name.
     */
    public static String getInputName(Method method) {
        GqlInput inputAnnotation = method.getAnnotation(GqlInput.class);
        if (inputAnnotation != null && !inputAnnotation.name().isEmpty()) {
            return inputAnnotation.name();
        }
        return method.getName();
    }

    /**
     * Gets name for GraphQL union type from {@link GqlUnionType} annotation.
     *
     * @param unionType <code>GqlUnionType</code> to get name from.
     * @return <code>String</code> with union type name.
     */
    public static String getUnionName(GqlUnionType unionType) {
        if (!unionType.name().isEmpty()) {
            return unionType.name();
        }
        return unionType.value().getSimpleName();
    }

    private static String addAliasIfPresent(String fieldName, GqlField fieldAnnotation) {
        String alias = fieldAnnotation.alias();
        return alias.isEmpty() ? fieldName : alias + ":" + fieldName;
    }

    private static String addArgumentsIfPresent(String fieldName, GqlField fieldAnnotation) {
        GqlFieldArgument[] arguments = fieldAnnotation.arguments();
        if (arguments.length != 0) {
            String argumentValues = Arrays.stream(arguments).map(GqlNamePicker::generateArgumentValue)
                    .collect(Collectors.joining(","));
            fieldName += "(" + argumentValues + ")";
        }
        return fieldName;
    }

    private static String generateArgumentValue(GqlFieldArgument argument) {
        return argument.name() + ":" + argument.value();
    }
}
