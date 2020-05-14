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
package com.github.vladislavsevruk.generator.strategy.picker.selection;

import lombok.Getter;

/**
 * TODO
 */
public enum SelectionSetGenerationStrategy {

    ALL_FIELDS(new AllFieldsPickingStrategy()),
    ONLY_ID(new OnlyIdFieldsPickingStrategy()),
    ONLY_NON_NULL(new OnlyNonNullFieldsPickingStrategy()),
    WITHOUT_SELECTION_SETS(new WithoutFieldsWithSelectionSetPickingStrategy());

    @Getter
    private FieldsPickingStrategy fieldsPickingStrategy;

    SelectionSetGenerationStrategy(FieldsPickingStrategy fieldsPickingStrategy) {
        this.fieldsPickingStrategy = fieldsPickingStrategy;
    }

    /**
     * TODO
     */
    public static SelectionSetGenerationStrategy allFields() {
        return ALL_FIELDS;
    }

    /**
     * TODO
     */
    public static SelectionSetGenerationStrategy defaultStrategy() {
        return allFields();
    }

    /**
     * TODO
     */
    public static SelectionSetGenerationStrategy fieldsWithoutSelectionSets() {
        return WITHOUT_SELECTION_SETS;
    }

    /**
     * TODO
     */
    public static SelectionSetGenerationStrategy onlyId() {
        return ONLY_ID;
    }

    /**
     * TODO
     */
    public static SelectionSetGenerationStrategy onlyNonNull() {
        return ONLY_NON_NULL;
    }
}
