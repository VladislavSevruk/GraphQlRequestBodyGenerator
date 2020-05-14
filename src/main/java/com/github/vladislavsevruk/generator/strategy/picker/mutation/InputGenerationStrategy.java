/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2020 Uladzislau Seuruk
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */
package com.github.vladislavsevruk.generator.strategy.picker.mutation;

import lombok.Getter;

/**
 * Contains predefined {@link InputFieldsPickingStrategy} for input argument:<ul>
 * <li> pick all fields
 * <li> pick only fields that have non-null value
 * </ul>
 *
 * @see InputFieldsPickingStrategy
 * @see AllInputFieldsPickingStrategy
 * @see WithoutNullsInputFieldsPickingStrategy
 */
public enum InputGenerationStrategy {

    ALL_FIELDS(new AllInputFieldsPickingStrategy()),
    WITHOUT_NULLS(new WithoutNullsInputFieldsPickingStrategy());

    @Getter
    private InputFieldsPickingStrategy inputFieldsPickingStrategy;

    InputGenerationStrategy(InputFieldsPickingStrategy inputFieldsPickingStrategy) {
        this.inputFieldsPickingStrategy = inputFieldsPickingStrategy;
    }

    /**
     * Returns strategy for all fields picking.
     */
    public static InputGenerationStrategy allFields() {
        return ALL_FIELDS;
    }

    /**
     * Returns default input fields picking strategy.
     */
    public static InputGenerationStrategy defaultStrategy() {
        return allFields();
    }

    /**
     * Returns strategy for picking only fields that have non-null value.
     */
    public static InputGenerationStrategy nonNullsFields() {
        return WITHOUT_NULLS;
    }
}
