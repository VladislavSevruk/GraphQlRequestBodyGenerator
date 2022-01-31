/*
 * MIT License
 *
 * Copyright (c) 2021 Uladzislau Seuruk
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

import com.github.vladislavsevruk.generator.GqlRequestBodyGenerator;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.strategy.looping.EndlessLoopBreakingStrategy;
import com.github.vladislavsevruk.generator.test.data.AnnotatedVariableAllMethodsTestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

class GqlNameUtilTest {

    private static final String BOOLEAN_TYPE_NAME = "Boolean";
    private static final String FLOAT_TYPE_NAME = "Float";
    private static final String INT_TYPE_NAME = "Int";
    private static final String STRING_TYPE_NAME = "String";

    @Test
    void bigDecimalGqlTypeTest() {
        BigDecimal value = BigDecimal.ONE;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(FLOAT_TYPE_NAME, typeName);
    }

    @Test
    void bigIntegerGqlTypeTest() {
        BigInteger value = BigInteger.ONE;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(INT_TYPE_NAME, typeName);
    }

    @Test
    void booleanGqlTypeTest() {
        boolean value = false;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(BOOLEAN_TYPE_NAME, typeName);
    }

    @Test
    void byteGqlTypeTest() {
        byte value = 1;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(INT_TYPE_NAME, typeName);
    }

    @Test
    void doubleIntegerGqlTypeTest() {
        double value = 1.0d;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(FLOAT_TYPE_NAME, typeName);
    }

    @Test
    void floatIntegerGqlTypeTest() {
        Float value = 1.0f;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(FLOAT_TYPE_NAME, typeName);
    }

    @Test
    void integerGqlTypeTest() {
        int value = 1;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(INT_TYPE_NAME, typeName);
    }

    @Test
    void longGqlTypeTest() {
        Long value = 1L;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(INT_TYPE_NAME, typeName);
    }

    @Test
    void modelGqlTypeTest() {
        AnnotatedVariableAllMethodsTestModel value = new AnnotatedVariableAllMethodsTestModel();
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(AnnotatedVariableAllMethodsTestModel.class.getSimpleName(), typeName);
    }

    @Test
    void shortGqlTypeTest() {
        Short value = 1;
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(INT_TYPE_NAME, typeName);
    }

    @Test
    void stringGqlTypeTest() {
        String value = "value";
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals(STRING_TYPE_NAME, typeName);
    }
}
