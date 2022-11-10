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
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaInterface;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.PrimitiveSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.parser.DelayedSchemaObjectInterfaceStorage;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaType;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnion;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnionType;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

class GqlSchemaTypeParserTest {

    @Test
    void canParseTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        Assertions.assertTrue(parser.canParse(objectType));
    }

    @Test
    void cannotParseUnionTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String notObjectType = GraphQlSchemaGenerator.getMultiUnion();
        Assertions.assertFalse(parser.canParse(notObjectType));
    }

    @Test
    void parseComplexObjectTypeAddPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaUnionType[] unionTypes = new GqlSchemaUnionType[0];
        GqlSchemaUnion schemaUnion = new GqlSchemaUnion("com.test", "SingleUnionPojo", unionTypes);
        when(schemaObjectStorage.get("SingleUnionPojo")).thenReturn(schemaUnion);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "SimpleEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get("SimpleEnumTypePojo")).thenReturn(schemaEnum);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getComplexType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("ComplexTypePojo", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(6, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get("SimpleTypePojo")).thenReturn(schemaObject2);
        verifyComplexObjectField1((GqlSchemaField) fields.get(0), schemaObject2);
        verifyComplexObjectField2((GqlSchemaField) fields.get(1), schemaObject2);
        verifyComplexObjectField3((GqlSchemaField) fields.get(2), schemaObject2);
        verifyComplexObjectField4((GqlSchemaField) fields.get(3), schemaEnum);
        verifyComplexObjectField5((GqlSchemaField) fields.get(4), schemaUnion);
        verifyComplexObjectField6((GqlSchemaField) fields.get(5), schemaUnion, unionTypes);
    }

    @Test
    void parseComplexObjectTypeAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaUnionType[] unionTypes = new GqlSchemaUnionType[0];
        GqlSchemaUnion schemaUnion = new GqlSchemaUnion("com.test", "TestSingleUnionPojo", unionTypes);
        when(schemaObjectStorage.get("TestSingleUnionPojo")).thenReturn(schemaUnion);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestSimpleEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get("TestSimpleEnumTypePojo")).thenReturn(schemaEnum);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getComplexType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("TestComplexTypePojo", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(6, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get("TestSimpleTypePojo")).thenReturn(schemaObject2);
        verifyComplexObjectField1((GqlSchemaField) fields.get(0), schemaObject2);
        verifyComplexObjectField2((GqlSchemaField) fields.get(1), schemaObject2);
        verifyComplexObjectField3((GqlSchemaField) fields.get(2), schemaObject2);
        verifyComplexObjectField4((GqlSchemaField) fields.get(3), schemaEnum);
        verifyComplexObjectField5((GqlSchemaField) fields.get(4), schemaUnion);
        verifyComplexObjectField6((GqlSchemaField) fields.get(5), schemaUnion, unionTypes);
    }

    @Test
    void parseComplexObjectTypeAddPrefixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaUnionType[] unionTypes = new GqlSchemaUnionType[0];
        GqlSchemaUnion schemaUnion = new GqlSchemaUnion("com.test", "TestSingleUnion", unionTypes);
        when(schemaObjectStorage.get("TestSingleUnion")).thenReturn(schemaUnion);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestSimpleEnumType", Collections.emptyList());
        when(schemaObjectStorage.get("TestSimpleEnumType")).thenReturn(schemaEnum);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getComplexType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("TestComplexType", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(6, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get("TestSimpleType")).thenReturn(schemaObject2);
        verifyComplexObjectField1((GqlSchemaField) fields.get(0), schemaObject2);
        verifyComplexObjectField2((GqlSchemaField) fields.get(1), schemaObject2);
        verifyComplexObjectField3((GqlSchemaField) fields.get(2), schemaObject2);
        verifyComplexObjectField4((GqlSchemaField) fields.get(3), schemaEnum);
        verifyComplexObjectField5((GqlSchemaField) fields.get(4), schemaUnion);
        verifyComplexObjectField6((GqlSchemaField) fields.get(5), schemaUnion, unionTypes);
    }

    @Test
    void parseComplexObjectTypeNotUpdateNamesToJavaStyleAddPrefixAndPostfixTest() {
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
        GqlSchemaUnionType[] unionTypes = new GqlSchemaUnionType[0];
        GqlSchemaUnion schemaUnion = new GqlSchemaUnion("com.test", "TestSingleUnionPojo", unionTypes);
        when(schemaObjectStorage.get("TestSingleUnionPojo")).thenReturn(schemaUnion);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestSimpleEnumTypePojo", Collections.emptyList());
        when(schemaObjectStorage.get("Testsimple_enum_typePojo")).thenReturn(schemaEnum);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getComplexType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("TestComplexTypePojo", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(6, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get("Testsimple_typePojo")).thenReturn(schemaObject2);
        verifyComplexObjectField1((GqlSchemaField) fields.get(0), schemaObject2);
        verifyComplexObjectField2((GqlSchemaField) fields.get(1), schemaObject2);
        verifyComplexObjectField3((GqlSchemaField) fields.get(2), schemaObject2);
        verifyComplexObjectField4((GqlSchemaField) fields.get(3), schemaEnum);
        verifyComplexObjectField5((GqlSchemaField) fields.get(4), schemaUnion);
        verifyComplexObjectField6((GqlSchemaField) fields.get(5), schemaUnion, unionTypes);
    }

    @Test
    void parseComplexObjectTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> useStringsInsteadOfEnums = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaUnionType[] unionTypes = new GqlSchemaUnionType[0];
        GqlSchemaUnion schemaUnion = new GqlSchemaUnion("com.test", "SingleUnion", unionTypes);
        when(schemaObjectStorage.get("SingleUnion")).thenReturn(schemaUnion);
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "SimpleEnumType", Collections.emptyList());
        when(schemaObjectStorage.get("SimpleEnumType")).thenReturn(schemaEnum);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getComplexType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("ComplexType", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertTrue(schemaType.getInterfaces().isEmpty());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(6, fields.size());
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get("SimpleType")).thenReturn(schemaObject2);
        verifyComplexObjectField1((GqlSchemaField) fields.get(0), schemaObject2);
        verifyComplexObjectField2((GqlSchemaField) fields.get(1), schemaObject2);
        verifyComplexObjectField3((GqlSchemaField) fields.get(2), schemaObject2);
        verifyComplexObjectField4((GqlSchemaField) fields.get(3), schemaEnum);
        verifyComplexObjectField5((GqlSchemaField) fields.get(4), schemaUnion);
        verifyComplexObjectField6((GqlSchemaField) fields.get(5), schemaUnion, unionTypes);
    }

    @Test
    @SuppressWarnings("unchecked")
    void parseSimpleObjectTypeDelayedInterfacesAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        SchemaInterface schemaInterface = Mockito.mock(SchemaInterface.class);
        Supplier<SchemaEntity> supplier = Mockito.mock(Supplier.class);
        when(supplier.get()).thenReturn(schemaInterface);
        when(interfaceStorage.get("TestSimpleTypePojo")).thenReturn(Collections.singletonList(supplier));
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("TestSimpleTypePojo", schemaType.getName());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertEquals(1, schemaType.getInterfaces().size());
        Assertions.assertEquals(schemaInterface, schemaType.getInterfaces().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void parseSimpleObjectTypeDelayedInterfacesNotUpdateNamesToJavaStyleAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        SchemaInterface schemaInterface = Mockito.mock(SchemaInterface.class);
        Supplier<SchemaEntity> supplier = Mockito.mock(Supplier.class);
        when(supplier.get()).thenReturn(schemaInterface);
        when(interfaceStorage.get("Testsimple_typePojo")).thenReturn(Collections.singletonList(supplier));
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("Testsimple_typePojo", schemaType.getName());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertEquals(1, schemaType.getInterfaces().size());
        Assertions.assertEquals(schemaInterface, schemaType.getInterfaces().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void parseSimpleObjectTypeDelayedInterfacesNotUpdateNamesToJavaStyleTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        SchemaInterface schemaInterface = Mockito.mock(SchemaInterface.class);
        Supplier<SchemaEntity> supplier = Mockito.mock(Supplier.class);
        when(supplier.get()).thenReturn(schemaInterface);
        when(interfaceStorage.get("simple_type")).thenReturn(Collections.singletonList(supplier));
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("simple_type", schemaType.getName());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertEquals(1, schemaType.getInterfaces().size());
        Assertions.assertEquals(schemaInterface, schemaType.getInterfaces().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void parseSimpleObjectTypeDelayedInterfacesTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        SchemaInterface schemaInterface = Mockito.mock(SchemaInterface.class);
        Supplier<SchemaEntity> supplier = Mockito.mock(Supplier.class);
        when(supplier.get()).thenReturn(schemaInterface);
        when(interfaceStorage.get("SimpleType")).thenReturn(Collections.singletonList(supplier));
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("SimpleType", schemaType.getName());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertEquals(1, schemaType.getInterfaces().size());
        Assertions.assertEquals(schemaInterface, schemaType.getInterfaces().get(0));
    }

    @Test
    void parseSimpleObjectTypeNotUpdateNamesToJavaStyleTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("simple_type", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        List<SchemaField> fields = schemaType.getFields();
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
    void parseSimpleObjectTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String objectType = GraphQlSchemaGenerator.getSimpleType();
        GqlSchemaType schemaType = parser.parse(objectType);
        Assertions.assertNotNull(schemaType);
        Assertions.assertEquals("SimpleType", schemaType.getName());
        Assertions.assertEquals("com.test", schemaType.getPackage());
        Assertions.assertNull(schemaType.getSuperclass());
        Assertions.assertNotNull(schemaType.getInterfaces());
        Assertions.assertTrue(schemaType.getInterfaces().isEmpty());
        List<SchemaField> fields = schemaType.getFields();
        Assertions.assertNotNull(fields);
        Assertions.assertEquals(5, fields.size());
        verifySimpleObjectField1((GqlSchemaField) fields.get(0));
        verifySimpleObjectField2((GqlSchemaField) fields.get(1));
        verifySimpleObjectField3((GqlSchemaField) fields.get(2));
        verifySimpleObjectField4((GqlSchemaField) fields.get(3));
        verifySimpleObjectField5((GqlSchemaField) fields.get(4));
    }

    @Test
    void parseUnsupportedEntityTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaTypeParser parser = new GqlSchemaTypeParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String notObjectType = GraphQlSchemaGenerator.getComplexInput();
        GqlEntityParsingException exception = Assertions.assertThrows(GqlEntityParsingException.class,
                () -> parser.parse(notObjectType));
        Assertions.assertEquals("Cannot parse to type entity: " + notObjectType, exception.getMessage());
    }

    private void verifyComplexObjectField1(GqlSchemaField field, SchemaObject schemaObject) {
        Assertions.assertEquals("value", field.getName());
        Assertions.assertEquals("value", field.getRawSchemaName());
        Assertions.assertEquals("value", field.getNameForJackson());
        Assertions.assertEquals(schemaObject, field.getType());
    }

    private void verifyComplexObjectField2(GqlSchemaField field, SchemaObject schemaObject) {
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
    }

    private void verifyComplexObjectField3(GqlSchemaField field, SchemaObject schemaObject) {
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values2", field.getRawSchemaName());
        Assertions.assertEquals("values2", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaObject,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
    }

    private void verifyComplexObjectField4(GqlSchemaField field, SchemaObject schemaObject) {
        Assertions.assertEquals("value3", field.getName());
        Assertions.assertEquals("value3", field.getRawSchemaName());
        Assertions.assertEquals("value3", field.getNameForJackson());
        Assertions.assertEquals(schemaObject, field.getType());
    }

    private void verifyComplexObjectField5(GqlSchemaField field, SchemaObject schemaObject) {
        Assertions.assertEquals("value4", field.getName());
        Assertions.assertEquals("value4", field.getRawSchemaName());
        Assertions.assertEquals("value4", field.getNameForJackson());
        Assertions.assertEquals(schemaObject, field.getType());
    }

    private void verifyComplexObjectField6(GqlSchemaField field, SchemaObject schemaUnion,
            GqlSchemaUnionType[] unionTypes) {
        Assertions.assertEquals("value5", field.getName());
        Assertions.assertEquals("value5", field.getRawSchemaName());
        Assertions.assertEquals("value5", field.getNameForJackson());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(schemaUnion, ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertEquals(unionTypes, field.getUnionTypes());
    }

    private void verifySimpleObjectField1(GqlSchemaField field) {
        Assertions.assertEquals("id", field.getName());
        Assertions.assertEquals("id", field.getRawSchemaName());
        Assertions.assertEquals("id", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.INT, field.getType());
        Assertions.assertNull(field.getUnionTypes());
    }

    private void verifySimpleObjectField2(GqlSchemaField field) {
        Assertions.assertEquals("values", field.getName());
        Assertions.assertEquals("values", field.getRawSchemaName());
        Assertions.assertEquals("values", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(CommonJavaSchemaEntity.STRING,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
    }

    private void verifySimpleObjectField3(GqlSchemaField field) {
        Assertions.assertEquals("values2", field.getName());
        Assertions.assertEquals("values_2", field.getRawSchemaName());
        Assertions.assertEquals("values_2", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(ArraySchemaEntity.class, field.getType().getClass());
        Assertions.assertEquals(CommonJavaSchemaEntity.INTEGER,
                ((ArraySchemaEntity) field.getType()).getElementTypes().iterator().next());
        Assertions.assertNull(field.getUnionTypes());
    }

    private void verifySimpleObjectField4(GqlSchemaField field) {
        Assertions.assertEquals("valUe3", field.getName());
        Assertions.assertEquals("val_ue3", field.getRawSchemaName());
        Assertions.assertEquals("val_ue3", field.getNameForJackson());
        Assertions.assertTrue(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.BOOLEAN, field.getType());
        Assertions.assertNull(field.getUnionTypes());
    }

    private void verifySimpleObjectField5(GqlSchemaField field) {
        Assertions.assertEquals("value4", field.getName());
        Assertions.assertEquals("value4", field.getRawSchemaName());
        Assertions.assertEquals("value4", field.getNameForJackson());
        Assertions.assertFalse(field.isNonNull());
        Assertions.assertFalse(field.isUnion());
        Assertions.assertEquals(PrimitiveSchemaEntity.DOUBLE, field.getType());
        Assertions.assertNull(field.getUnionTypes());
    }
}
