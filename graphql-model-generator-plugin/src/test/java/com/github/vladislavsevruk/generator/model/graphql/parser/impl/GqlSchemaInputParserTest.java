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
package com.github.vladislavsevruk.generator.model.graphql.parser.impl;

import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.PrimitiveSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaInput;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GqlSchemaInputParserTest {

    @Test
    void canParseInputTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleInput();
        Assertions.assertTrue(parser.canParse(objectType));
    }

    @Test
    void cannotParseNotInputTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String notObjectType = GraphQlSchemaGenerator.getSimpleType();
        Assertions.assertFalse(parser.canParse(notObjectType));
    }

    @Test
    void parseComplexInputAddPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "ComplexEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get(eq("ComplexEnumTypePojo"))).thenReturn(schemaEnum);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getComplexInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("ComplexInputPojo", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(4, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("SimpleInputPojo"))).thenReturn(schemaObject2);
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertEquals(schemaObject2, field.getType());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertEquals(schemaEnum, field.getType());
    }

    @Test
    void parseComplexInputAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestComplexEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get(eq("TestComplexEnumTypePojo"))).thenReturn(schemaEnum);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getComplexInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("TestComplexInputPojo", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(4, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("TestSimpleInputPojo"))).thenReturn(schemaObject2);
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertEquals(schemaObject2, field.getType());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertEquals(schemaEnum, field.getType());
    }

    @Test
    void parseComplexInputAddPrefixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestComplexEnumType", Collections.emptyList());
        when(schemaObjectStorage.get(eq("TestComplexEnumType"))).thenReturn(schemaEnum);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getComplexInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("TestComplexInput", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(4, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("TestSimpleInput"))).thenReturn(schemaObject2);
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertEquals(schemaObject2, field.getType());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertEquals(schemaEnum, field.getType());
    }

    @Test
    void parseComplexInputNotUpdateNamesToJavaStyleAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestComplexEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get(eq("TestComplexEnumTypePojo"))).thenReturn(schemaEnum);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getComplexInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("TestComplexInputPojo", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(4, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("Testsimple_inputPojo"))).thenReturn(schemaObject2);
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertEquals(schemaObject2, field.getType());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertEquals(schemaEnum, field.getType());
    }

    @Test
    void parseComplexInputTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "ComplexEnumType", Collections.emptyList());
        when(schemaObjectStorage.get(eq("ComplexEnumType"))).thenReturn(schemaEnum);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getComplexInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("ComplexInput", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        Assertions.assertNull(schemaInput.getSuperclass());
        Assertions.assertNotNull(schemaInput.getInterfaces());
        Assertions.assertTrue(schemaInput.getInterfaces().isEmpty());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(4, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("SimpleInput"))).thenReturn(schemaObject2);
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(schemaObject2, field.getType());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject2,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(schemaEnum, field.getType());
        Assertions.assertNull(field.getUnionTypes());
    }

    @Test
    void parseSimpleInputNotUpdateNamesToJavaStyleTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("simple_input", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(5, fields.size());
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("id", field.getName());
        Assertions.assertEquals("id", field.getRawSchemaName());
        Assertions.assertEquals("id", field.getNameForJackson());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values_2", field.getName());
        Assertions.assertEquals("values_2", field.getRawSchemaName());
        Assertions.assertEquals("values_2", field.getNameForJackson());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("val_ue3", field.getName());
        Assertions.assertEquals("val_ue3", field.getRawSchemaName());
        Assertions.assertEquals("val_ue3", field.getNameForJackson());
        field = (GqlSchemaField) fields.get(4);
        Assertions.assertEquals("value4", field.getName());
        Assertions.assertEquals("value4", field.getRawSchemaName());
        Assertions.assertEquals("value4", field.getNameForJackson());
    }

    @Test
    void parseSimpleInputTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleInput();
        GqlSchemaInput schemaInput = parser.parse(objectType);
        Assertions.assertNotNull(schemaInput);
        Assertions.assertEquals("SimpleInput", schemaInput.getName());
        Assertions.assertEquals("com.test", schemaInput.getPackage());
        Assertions.assertNull(schemaInput.getSuperclass());
        Assertions.assertNotNull(schemaInput.getInterfaces());
        Assertions.assertTrue(schemaInput.getInterfaces().isEmpty());
        List<SchemaField> fields = schemaInput.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(5, fields.size());
        GqlSchemaField field = (GqlSchemaField) fields.get(0);
        Assertions.assertEquals("id", field.getName());
        Assertions.assertEquals("id", field.getRawSchemaName());
        Assertions.assertEquals("id", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.INT, field.getType());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(1);
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(CommonJavaSchemaEntity.STRING,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(2);
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values_2", field.getRawSchemaName());
        Assertions.assertEquals("values_2", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(CommonJavaSchemaEntity.INTEGER,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(3);
        Assertions.assertEquals("valUe3", field.getName());
        Assertions.assertEquals("val_ue3", field.getRawSchemaName());
        Assertions.assertEquals("val_ue3", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.BOOLEAN, field.getType());
        Assertions.assertNull(field.getUnionTypes());
        field = (GqlSchemaField) fields.get(4);
        Assertions.assertEquals("value4", field.getName());
        Assertions.assertEquals("value4", field.getRawSchemaName());
        Assertions.assertEquals("value4", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.DOUBLE, field.getType());
        Assertions.assertNull(field.getUnionTypes());
    }

    @Test
    void parseUnsupportedEntityTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaInputParser parser = new GqlSchemaInputParser(pluginExtension, schemaObjectStorage);
        String notInputType = GraphQlSchemaGenerator.getComplexType();
        GqlEntityParsingException exception = Assertions.assertThrows(GqlEntityParsingException.class,
                () -> parser.parse(notInputType));
        Assertions.assertEquals("Cannot parse to input entity: " + notInputType, exception.getMessage());
    }
}
