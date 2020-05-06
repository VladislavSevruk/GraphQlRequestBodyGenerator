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
package com.github.vladislavsevruk.generator.util;

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlQuery;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Picks name for GraphQL queries and fields.
 */
public class GqlNamePicker {

    private GqlNamePicker() {
    }

    /**
     * Gets name for GraphQL query from {@link GqlField}  annotation if present or using field name.
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
     * Gets name for GraphQL query from {@link GqlQuery} annotation if present or using class name.
     *
     * @param clazz <code>Class</code> that represents GraphQL query model to get name for.
     * @return <code>String</code> with query name.
     */
    public static String getQueryName(Class<?> clazz) {
        GqlQuery annotation = clazz.getAnnotation(GqlQuery.class);
        if (Objects.nonNull(annotation)) {
            return annotation.name();
        }
        return clazz.getSimpleName();
    }
}
