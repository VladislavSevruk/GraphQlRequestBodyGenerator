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
package com.github.vladislavsevruk.generator.strategy.picker;

import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class OnlyMandatoryFieldsPickingStrategyTest {

    private OnlyMandatoryFieldsPickingStrategy strategy = new OnlyMandatoryFieldsPickingStrategy();

    @Test
    public void fieldWithGqlDelegateAnnotationTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithDelegateAnnotation");
        Assertions.assertTrue(strategy.shouldBePicked(field));
    }

    @Test
    public void fieldWithGqlEntityAnnotationTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithEntityAnnotation");
        Assertions.assertFalse(strategy.shouldBePicked(field));
    }

    @Test
    public void fieldWithGqlFieldAnnotationTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithFieldAnnotation");
        Assertions.assertFalse(strategy.shouldBePicked(field));
    }

    @Test
    public void fieldWithGqlIgnoreAnnotationTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithIgnoreAnnotation");
        Assertions.assertFalse(strategy.shouldBePicked(field));
    }

    @Test
    public void fieldWithoutAnnotations() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("fieldWithoutAnnotations");
        Assertions.assertFalse(strategy.shouldBePicked(field));
    }

    @Test
    public void mandatoryEntityTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("mandatoryEntity");
        Assertions.assertTrue(strategy.shouldBePicked(field));
    }

    @Test
    public void mandatoryFieldTest() throws NoSuchFieldException {
        Field field = TestModelWithAnnotations.class.getDeclaredField("mandatoryField");
        Assertions.assertTrue(strategy.shouldBePicked(field));
    }
}
