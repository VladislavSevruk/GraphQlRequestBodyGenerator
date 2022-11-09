/*
 * MIT License
 *
 * Copyright (c) 2022 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.model.graphql.resolver;

import com.github.vladislavsevruk.generator.java.type.SchemaElementSequence;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.PrimitiveSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.CollectionSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.IterableSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ListSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.SetSchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.constant.ElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlFloatType;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlIntType;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlScalarType;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaInput;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaType;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

class GqlSchemaInputResolverTest {

    static Stream<Arguments> arrayProvider() {
        return Stream.of(Arguments.of(ArraySchemaEntity.class, ElementSequence.ARRAY),
                Arguments.of(CollectionSchemaEntity.class, ElementSequence.COLLECTION),
                Arguments.of(IterableSchemaEntity.class, ElementSequence.ITERABLE),
                Arguments.of(ListSchemaEntity.class, ElementSequence.LIST),
                Arguments.of(SetSchemaEntity.class, ElementSequence.SET));
    }

    static Stream<Arguments> floatTypesArrayProvider() {
        return Stream.of(
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.BIG_DECIMAL, ElementSequence.ARRAY,
                        GqlFloatType.BIG_DECIMAL),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.BIG_DECIMAL,
                        ElementSequence.COLLECTION, GqlFloatType.BIG_DECIMAL),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.BIG_DECIMAL, ElementSequence.ITERABLE,
                        GqlFloatType.BIG_DECIMAL),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.BIG_DECIMAL, ElementSequence.LIST,
                        GqlFloatType.BIG_DECIMAL),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.BIG_DECIMAL, ElementSequence.SET,
                        GqlFloatType.BIG_DECIMAL),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.DOUBLE, ElementSequence.ARRAY,
                        GqlFloatType.DOUBLE),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.DOUBLE, ElementSequence.COLLECTION,
                        GqlFloatType.DOUBLE),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.DOUBLE, ElementSequence.ITERABLE,
                        GqlFloatType.DOUBLE),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.DOUBLE, ElementSequence.LIST,
                        GqlFloatType.DOUBLE),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.DOUBLE, ElementSequence.SET,
                        GqlFloatType.DOUBLE),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.FLOAT, ElementSequence.ARRAY,
                        GqlFloatType.FLOAT),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.FLOAT, ElementSequence.COLLECTION,
                        GqlFloatType.FLOAT),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.FLOAT, ElementSequence.ITERABLE,
                        GqlFloatType.FLOAT),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.FLOAT, ElementSequence.LIST,
                        GqlFloatType.FLOAT),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.FLOAT, ElementSequence.SET,
                        GqlFloatType.FLOAT),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ARRAY,
                        GqlFloatType.STRING),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.COLLECTION,
                        GqlFloatType.STRING),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ITERABLE,
                        GqlFloatType.STRING),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.LIST,
                        GqlFloatType.STRING),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.SET,
                        GqlFloatType.STRING));
    }

    static Stream<Arguments> integerTypesArrayProvider() {
        return Stream.of(
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.BIG_INTEGER, ElementSequence.ARRAY,
                        GqlIntType.BIG_INTEGER),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.BIG_INTEGER,
                        ElementSequence.COLLECTION, GqlIntType.BIG_INTEGER),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.BIG_INTEGER, ElementSequence.ITERABLE,
                        GqlIntType.BIG_INTEGER),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.BIG_INTEGER, ElementSequence.LIST,
                        GqlIntType.BIG_INTEGER),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.BIG_INTEGER, ElementSequence.SET,
                        GqlIntType.BIG_INTEGER),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.INTEGER, ElementSequence.ARRAY,
                        GqlIntType.INTEGER),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.INTEGER, ElementSequence.COLLECTION,
                        GqlIntType.INTEGER),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.INTEGER, ElementSequence.ITERABLE,
                        GqlIntType.INTEGER),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.INTEGER, ElementSequence.LIST,
                        GqlIntType.INTEGER),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.INTEGER, ElementSequence.SET,
                        GqlIntType.INTEGER),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.LONG, ElementSequence.ARRAY,
                        GqlIntType.LONG),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.LONG, ElementSequence.COLLECTION,
                        GqlIntType.LONG),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.LONG, ElementSequence.ITERABLE,
                        GqlIntType.LONG),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.LONG, ElementSequence.LIST,
                        GqlIntType.LONG),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.LONG, ElementSequence.SET, GqlIntType.LONG),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ARRAY,
                        GqlIntType.STRING),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.COLLECTION,
                        GqlIntType.STRING),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ITERABLE,
                        GqlIntType.STRING),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.LIST,
                        GqlIntType.STRING),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.SET,
                        GqlIntType.STRING));
    }

    static Stream<Arguments> primitiveFloatTypesProvider() {
        return Stream.of(Arguments.of(CommonJavaSchemaEntity.BIG_DECIMAL, GqlFloatType.BIG_DECIMAL),
                Arguments.of(PrimitiveSchemaEntity.DOUBLE, GqlFloatType.DOUBLE),
                Arguments.of(PrimitiveSchemaEntity.FLOAT, GqlFloatType.FLOAT),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlFloatType.STRING));
    }

    static Stream<Arguments> primitiveIntegerTypesProvider() {
        return Stream.of(Arguments.of(CommonJavaSchemaEntity.BIG_INTEGER, GqlIntType.BIG_INTEGER),
                Arguments.of(PrimitiveSchemaEntity.INT, GqlIntType.INTEGER),
                Arguments.of(PrimitiveSchemaEntity.LONG, GqlIntType.LONG),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlIntType.STRING));
    }

    static Stream<Arguments> primitiveScalarTypesProvider() {
        return Stream.of(Arguments.of(PrimitiveSchemaEntity.BOOLEAN, GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlScalarType.STRING_TYPE));
    }

    static Stream<Arguments> scalarTypesArrayProvider() {
        return Stream.of(Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.BOOLEAN, ElementSequence.ARRAY,
                        GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.BOOLEAN, ElementSequence.COLLECTION,
                        GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.BOOLEAN, ElementSequence.ITERABLE,
                        GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.BOOLEAN, ElementSequence.LIST,
                        GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.BOOLEAN, ElementSequence.SET,
                        GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(ArraySchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ARRAY,
                        GqlScalarType.STRING_TYPE),
                Arguments.of(CollectionSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.COLLECTION,
                        GqlScalarType.STRING_TYPE),
                Arguments.of(IterableSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.ITERABLE,
                        GqlScalarType.STRING_TYPE),
                Arguments.of(ListSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.LIST,
                        GqlScalarType.STRING_TYPE),
                Arguments.of(SetSchemaEntity.class, CommonJavaSchemaEntity.STRING, ElementSequence.SET,
                        GqlScalarType.STRING_TYPE));
    }

    static Stream<Arguments> wrapperFloatTypesProvider() {
        return Stream.of(Arguments.of(CommonJavaSchemaEntity.BIG_DECIMAL, GqlFloatType.BIG_DECIMAL),
                Arguments.of(CommonJavaSchemaEntity.DOUBLE, GqlFloatType.DOUBLE),
                Arguments.of(CommonJavaSchemaEntity.FLOAT, GqlFloatType.FLOAT),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlFloatType.STRING));
    }

    static Stream<Arguments> wrapperIntegerTypesProvider() {
        return Stream.of(Arguments.of(CommonJavaSchemaEntity.BIG_INTEGER, GqlIntType.BIG_INTEGER),
                Arguments.of(CommonJavaSchemaEntity.INTEGER, GqlIntType.INTEGER),
                Arguments.of(CommonJavaSchemaEntity.LONG, GqlIntType.LONG),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlIntType.STRING));
    }

    static Stream<Arguments> wrapperScalarTypesProvider() {
        return Stream.of(Arguments.of(CommonJavaSchemaEntity.BOOLEAN, GqlScalarType.BOOLEAN_TYPE),
                Arguments.of(CommonJavaSchemaEntity.STRING, GqlScalarType.STRING_TYPE));
    }

    @ParameterizedTest
    @MethodSource("arrayProvider")
    void pickJavaTypeArrayTest(Class<?> expectedSequenceClass, ElementSequence elementSequence) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaType schemaType = new GqlSchemaType("com.test", "TestType", Collections.emptyList(),
                Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaType, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(schemaType, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("arrayProvider")
    void pickJavaTypeEnumArrayTest(Class<?> expectedSequenceClass, ElementSequence elementSequence) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStrings = TestExtensionUtil.mockProperty(false);
        when(extension.getUseStringsInsteadOfEnums()).thenReturn(useStrings);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaEnum, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(schemaEnum, sequenceEntity.getElementTypes().iterator().next());
    }

    @Test
    void pickJavaTypeEnumTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStrings = TestExtensionUtil.mockProperty(false);
        when(extension.getUseStringsInsteadOfEnums()).thenReturn(useStrings);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaEnum, false);
        Assertions.assertEquals(schemaEnum, result);
    }

    @ParameterizedTest
    @MethodSource("arrayProvider")
    void pickJavaTypeInputArrayTest(Class<?> expectedSequenceClass, ElementSequence elementSequence) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaInput schemaInput = new GqlSchemaInput("com.test", "TestInput", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaInput, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(schemaInput, sequenceEntity.getElementTypes().iterator().next());
    }

    @Test
    void pickJavaTypeInputTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaInput schemaInput = new GqlSchemaInput("com.test", "TestInput", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaInput, false);
        Assertions.assertEquals(schemaInput, result);
    }

    @ParameterizedTest
    @MethodSource("floatTypesArrayProvider")
    void pickJavaTypePrimitiveFloatArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlFloatType floatType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlFloatType> floatTypeProperty = TestExtensionUtil.mockProperty(floatType);
        when(extension.getTreatFloatAs()).thenReturn(floatTypeProperty);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.FLOAT_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("primitiveFloatTypesProvider")
    void pickJavaTypePrimitiveFloatTest(SchemaEntity expectedResult, GqlFloatType floatType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlFloatType> floatTypeProperty = TestExtensionUtil.mockProperty(floatType);
        when(extension.getTreatFloatAs()).thenReturn(floatTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.FLOAT_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("integerTypesArrayProvider")
    void pickJavaTypePrimitiveIdArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlIntType idType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> idTypeProperty = TestExtensionUtil.mockProperty(idType);
        when(extension.getTreatIdAs()).thenReturn(idTypeProperty);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.ID_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("primitiveIntegerTypesProvider")
    void pickJavaTypePrimitiveIdTest(SchemaEntity expectedResult, GqlIntType idType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> idTypeProperty = TestExtensionUtil.mockProperty(idType);
        when(extension.getTreatIdAs()).thenReturn(idTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.ID_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("integerTypesArrayProvider")
    void pickJavaTypePrimitiveIntegerArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlIntType intType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> intTypeProperty = TestExtensionUtil.mockProperty(intType);
        when(extension.getTreatIntAs()).thenReturn(intTypeProperty);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.INT_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("primitiveIntegerTypesProvider")
    void pickJavaTypePrimitiveIntegerTest(SchemaEntity expectedResult, GqlIntType intType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> intTypeProperty = TestExtensionUtil.mockProperty(intType);
        when(extension.getTreatIntAs()).thenReturn(intTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.INT_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("scalarTypesArrayProvider")
    void pickJavaTypePrimitiveScalarArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlScalarType scalarType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(scalarType, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("primitiveScalarTypesProvider")
    void pickJavaTypePrimitiveScalarTest(SchemaEntity expectedResult, GqlScalarType scalarType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(true);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(scalarType, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void pickJavaTypeTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaType schemaType = new GqlSchemaType("com.test", "TestType", Collections.emptyList(),
                Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaType, false);
        Assertions.assertEquals(schemaType, result);
    }

    @Test
    void pickJavaTypeUseStringsInsteadOfEnumsArrayTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStrings = TestExtensionUtil.mockProperty(true);
        when(extension.getUseStringsInsteadOfEnums()).thenReturn(useStrings);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(ElementSequence.ARRAY);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaEnum, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ArraySchemaEntity.class, result.getClass());
        ArraySchemaEntity arraySchemaEntity = (ArraySchemaEntity) result;
        Assertions.assertNotNull(arraySchemaEntity.getElementTypes());
        Assertions.assertEquals(1, arraySchemaEntity.getElementTypes().size());
        Assertions.assertEquals(schemaEnum, arraySchemaEntity.getElementTypes().iterator().next());
    }

    @Test
    void pickJavaTypeUseStringsInsteadOfEnumsTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStrings = TestExtensionUtil.mockProperty(true);
        when(extension.getUseStringsInsteadOfEnums()).thenReturn(useStrings);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaEntity result = resolver.pickJavaType(schemaEnum, false);
        Assertions.assertEquals(schemaEnum, result);
    }

    @ParameterizedTest
    @MethodSource("floatTypesArrayProvider")
    void pickJavaTypeWrapperFloatArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlFloatType floatType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        Property<GqlFloatType> floatTypeProperty = TestExtensionUtil.mockProperty(floatType);
        when(extension.getTreatFloatAs()).thenReturn(floatTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.FLOAT_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("wrapperFloatTypesProvider")
    void pickJavaTypeWrapperFloatTest(SchemaEntity expectedResult, GqlFloatType floatType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlFloatType> floatTypeProperty = TestExtensionUtil.mockProperty(floatType);
        when(extension.getTreatFloatAs()).thenReturn(floatTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.FLOAT_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("integerTypesArrayProvider")
    void pickJavaTypeWrapperIdArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlIntType idType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        Property<GqlIntType> idTypeProperty = TestExtensionUtil.mockProperty(idType);
        when(extension.getTreatIdAs()).thenReturn(idTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.ID_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("wrapperIntegerTypesProvider")
    void pickJavaTypeWrapperIdTest(SchemaEntity expectedResult, GqlIntType idType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> idTypeProperty = TestExtensionUtil.mockProperty(idType);
        when(extension.getTreatIdAs()).thenReturn(idTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.ID_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("integerTypesArrayProvider")
    void pickJavaTypeWrapperIntegerArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlIntType intType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        Property<GqlIntType> intTypeProperty = TestExtensionUtil.mockProperty(intType);
        when(extension.getTreatIntAs()).thenReturn(intTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.INT_TYPE, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("wrapperIntegerTypesProvider")
    void pickJavaTypeWrapperIntegerTest(SchemaEntity expectedResult, GqlIntType intType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<GqlIntType> intTypeProperty = TestExtensionUtil.mockProperty(intType);
        when(extension.getTreatIntAs()).thenReturn(intTypeProperty);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(GqlScalarType.INT_TYPE, false);
        Assertions.assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("scalarTypesArrayProvider")
    void pickJavaTypeWrapperScalarArrayTest(Class<?> expectedSequenceClass, SchemaEntity expectedElementEntity,
            ElementSequence elementSequence, GqlScalarType scalarType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        Property<ElementSequence> treatArrayAs = TestExtensionUtil.mockProperty(elementSequence);
        when(extension.getTreatArrayAs()).thenReturn(treatArrayAs);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(scalarType, true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedSequenceClass, result.getClass());
        SchemaElementSequence sequenceEntity = (SchemaElementSequence) result;
        Assertions.assertNotNull(sequenceEntity.getElementTypes());
        Assertions.assertEquals(1, sequenceEntity.getElementTypes().size());
        Assertions.assertEquals(expectedElementEntity, sequenceEntity.getElementTypes().iterator().next());
    }

    @ParameterizedTest
    @MethodSource("wrapperScalarTypesProvider")
    void pickJavaTypeWrapperScalarTest(SchemaEntity expectedResult, GqlScalarType scalarType) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> usePrimitives = TestExtensionUtil.mockProperty(false);
        when(extension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitives);
        GqlSchemaInputResolver resolver = new GqlSchemaInputResolver(extension);
        SchemaEntity result = resolver.pickJavaType(scalarType, false);
        Assertions.assertEquals(expectedResult, result);
    }
}
