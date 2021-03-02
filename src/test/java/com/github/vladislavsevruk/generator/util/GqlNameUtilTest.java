package com.github.vladislavsevruk.generator.util;

import com.github.vladislavsevruk.generator.test.data.TestDataForInputTypeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        TestDataForInputTypeStrategy value = new TestDataForInputTypeStrategy();
        String typeName = GqlNamePicker.getGqlTypeName(value);
        Assertions.assertEquals("TestDataForInputTypeStrategy", typeName);
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
