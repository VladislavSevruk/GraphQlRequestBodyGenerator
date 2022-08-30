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
package com.github.vladislavsevruk.generator.util;

import com.github.vladislavsevruk.generator.annotation.GqlIgnore;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Gets argument values for GraphQL operations from fields and methods using reflection.
 */
@Log4j2
public final class ArgumentValueUtil {

    private static final String GETTER_PREFIX = "get";

    private ArgumentValueUtil() {
    }

    /**
     * Gets value for received field from received object.
     *
     * @param field <code>Field</code> to get value for.
     * @param value <code>Object</code> of class that contains field.
     * @return <code>Object</code> received from field directly or related getter method.
     */
    public static Object getValue(Field field, Object value) {
        Method getterMethod = findGetterMethod(field);
        if (getterMethod != null) {
            log.debug("Found '{}' getter method for '{}' field.", getterMethod.getName(), field.getName());
            return getValueByMethod(value, getterMethod);
        } else {
            log.debug("There was no getter method for '{}' field found.", field.getName());
            return getValueByField(value, field);
        }
    }

    /**
     * Gets value for received field from received object.
     *
     * @param value <code>Object</code> of class that contains field.
     * @param field <code>Field</code> to get value for.
     * @return <code>Object</code> received from field.
     */
    public static Object getValueByField(Object value, Field field) {
        boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(value);
        } catch (ReflectiveOperationException roEx) {
            log.error(String.format("Failed to get value of '%s' field.", field.getName()), roEx);
            return null;
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * Gets value for received method from received object.
     *
     * @param value        <code>Object</code> of class that contains method.
     * @param getterMethod getter <code>Method</code> to get value from.
     * @return <code>Object</code> received from method.
     */
    public static Object getValueByMethod(Object value, Method getterMethod) {
        try {
            return getterMethod.invoke(value);
        } catch (ReflectiveOperationException roEx) {
            log.error(String.format("Failed to execute '%s' getter method.", getterMethod.getName()), roEx);
            return null;
        }
    }

    private static boolean canBeGetter(Method method) {
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        Class<?> returnType = method.getReturnType();
        return !(returnType.equals(void.class) || returnType.equals(Void.class));
    }

    private static Method findGetterMethod(Field field) {
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

    private static String getNameFromAnnotation(Method method) {
        GqlInput fieldAnnotation = method.getAnnotation(GqlInput.class);
        return fieldAnnotation != null ? fieldAnnotation.name() : "";
    }

    private static String removeGetterPrefixIfPresent(String methodName) {
        if (methodName.startsWith(GETTER_PREFIX)) {
            int prefixLength = GETTER_PREFIX.length();
            return methodName.substring(prefixLength, prefixLength + 1).toLowerCase() + methodName.substring(
                    prefixLength + 1);
        }
        return methodName;
    }
}
