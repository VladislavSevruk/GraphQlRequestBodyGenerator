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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Contains utility methods for getting superclasses and implemented interfaces.
 */
public final class ClassUtil {

    private ClassUtil() {
    }

    /**
     * Gets the closest common type of iterable elements.
     *
     * @param values <code>Iterable</code> with elements to get common type of.
     * @param <T>    iterable elements type.
     * @return <code>Class</code> that is the closest common type of iterable elements.
     */
    public static <T> Class<? extends T> getCommonClass(Iterable<T> values) {
        Set<Class<?>> classes = StreamSupport.stream(values.spliterator(), false).filter(Objects::nonNull)
                .map(Object::getClass).collect(Collectors.toSet());
        return getCommonClass(classes);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> getCommonClass(Set<Class<?>> classes) {
        if (classes.isEmpty() || classes.contains(Object.class)) {
            return (Class<T>) Object.class;
        }
        if (classes.size() == 1) {
            return (Class<? extends T>) classes.iterator().next();
        }
        return (Class<? extends T>) getCommonSuperclass(classes);
    }

    private static Class<?> getCommonSuperclass(Set<Class<?>> classes) {
        Class<?> clazz = classes.iterator().next();
        do {
            if (classes.stream().allMatch(clazz::isAssignableFrom)) {
                return clazz;
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        return Object.class;
    }
}
