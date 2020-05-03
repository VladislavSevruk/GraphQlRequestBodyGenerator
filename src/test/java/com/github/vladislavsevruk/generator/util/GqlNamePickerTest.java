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

import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithoutAnnotations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class GqlNamePickerTest {

    @Test
    public void pickNameFoFieldWithoutAnnotationTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithoutAnnotations");
        String pickedName = GqlNamePicker.getFieldName(field);
        Assertions.assertEquals("fieldWithoutAnnotations", pickedName);
    }

    @Test
    public void pickNameForEntityWithAnnotationWithNameTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("namedEntity");
        String pickedName = GqlNamePicker.getFieldName(field);
        Assertions.assertEquals("customNamedEntity", pickedName);
    }

    @Test
    public void pickNameForEntityWithAnnotationWithoutNameTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("nonNullableEntity");
        String pickedName = GqlNamePicker.getFieldName(field);
        Assertions.assertEquals("nonNullableEntity", pickedName);
    }

    @Test
    public void pickNameForFieldWithAnnotationWithNameTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("namedField");
        String pickedName = GqlNamePicker.getFieldName(field);
        Assertions.assertEquals("customNamedField", pickedName);
    }

    @Test
    public void pickNameForFieldWithAnnotationWithoutNameTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("nonNullableField");
        String pickedName = GqlNamePicker.getFieldName(field);
        Assertions.assertEquals("nonNullableField", pickedName);
    }

    @Test
    public void pickNameForModelWithAnnotationTest() {
        String pickedName = GqlNamePicker.getQueryName(TestModelWithAnnotations.class);
        Assertions.assertEquals("customGqlQuery", pickedName);
    }

    @Test
    public void pickNameForModelWithoutAnnotationTest() {
        String pickedName = GqlNamePicker.getQueryName(TestModelWithoutAnnotations.class);
        Assertions.assertEquals("TestModelWithoutAnnotations", pickedName);
    }
}
