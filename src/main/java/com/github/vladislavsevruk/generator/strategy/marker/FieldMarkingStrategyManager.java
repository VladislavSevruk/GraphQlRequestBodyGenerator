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
package com.github.vladislavsevruk.generator.strategy.marker;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;

/**
 * Manages model fields marking strategies.<br> There are two field marking strategies for GraphQL operation body
 * generation:<ul>
 * <li> pick all fields except ones that have {@link GqlIgnore} annotation
 * <li> pick only fields that have {@link GqlField} or {@link GqlDelegate} annotations
 * </ul>
 *
 * @see GqlField
 * @see GqlDelegate
 * @see GqlIgnore
 */
public interface FieldMarkingStrategyManager {

    /**
     * Returns current picking strategy.
     */
    FieldMarkingStrategy getStrategy();

    /**
     * Sets picking all fields except ones that marked by {@link GqlIgnore} annotation as current picking strategy.
     */
    void useAllExceptIgnoredFieldsStrategy();

    /**
     * Sets custom marking strategy.
     */
    void useCustomStrategy(FieldMarkingStrategy customStrategy);

    /**
     * Sets picking only fields that marked by {@link GqlField} or {@link GqlDelegate} annotations as current picking
     * strategy.
     */
    void useOnlyMarkedFieldsStrategy();
}
