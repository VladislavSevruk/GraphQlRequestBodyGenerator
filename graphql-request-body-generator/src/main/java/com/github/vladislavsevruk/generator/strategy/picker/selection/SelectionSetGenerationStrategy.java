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
package com.github.vladislavsevruk.generator.strategy.picker.selection;

import lombok.Getter;

/**
 * Contains predefined {@link FieldsPickingStrategy} for selection set:<ul>
 * <li> pick all fields
 * <li> pick only 'id' fields itself or fields with nested 'id' field
 * <li> pick only ones that was marked as non-null fields
 * <li> pick only fields that do not have nested fields
 * </ul>
 *
 * @see FieldsPickingStrategy
 * @see AllFieldsPickingStrategy
 * @see OnlyIdFieldsPickingStrategy
 * @see OnlyNonNullFieldsPickingStrategy
 * @see WithoutFieldsWithSelectionSetPickingStrategy
 */
public enum SelectionSetGenerationStrategy {

    ALL_FIELDS(new AllFieldsPickingStrategy()),
    ONLY_ID(new OnlyIdFieldsPickingStrategy()),
    ONLY_NON_NULL(new OnlyNonNullFieldsPickingStrategy()),
    WITHOUT_SELECTION_SETS(new WithoutFieldsWithSelectionSetPickingStrategy());

    @Getter
    private final FieldsPickingStrategy fieldsPickingStrategy;

    SelectionSetGenerationStrategy(FieldsPickingStrategy fieldsPickingStrategy) {
        this.fieldsPickingStrategy = fieldsPickingStrategy;
    }

    /**
     * Returns strategy for all fields picking.
     */
    public static SelectionSetGenerationStrategy allFields() {
        return ALL_FIELDS;
    }

    /**
     * Returns default fields picking strategy.
     */
    public static SelectionSetGenerationStrategy defaultStrategy() {
        return allFields();
    }

    /**
     * Returns strategy for picking only fields that do not have nested fields.
     */
    public static SelectionSetGenerationStrategy fieldsWithoutSelectionSets() {
        return WITHOUT_SELECTION_SETS;
    }

    /**
     * Returns strategy for picking only 'id' fields itself or fields with nested 'id' field.
     */
    public static SelectionSetGenerationStrategy onlyId() {
        return ONLY_ID;
    }

    /**
     * Returns strategy for picking only ones that was marked as non-null fields.
     */
    public static SelectionSetGenerationStrategy onlyNonNull() {
        return ONLY_NON_NULL;
    }
}
