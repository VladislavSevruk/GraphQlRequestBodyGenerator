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

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;

import java.lang.reflect.Field;

/**
 * Provides selection set generation strategy for picking only 'id' fields itself or fields with nested 'id' field.
 */
public class OnlyIdFieldsPickingStrategy implements FieldsPickingStrategy {

    private static final String ID = "id";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldBePicked(Field field) {
        if (field.getAnnotation(GqlDelegate.class) != null) {
            return true;
        }
        GqlField fieldAnnotation = field.getAnnotation(GqlField.class);
        if (fieldAnnotation != null && fieldAnnotation.withSelectionSet()) {
            return true;
        }
        return GqlNamePicker.getFieldName(field).equals(ID);
    }
}
