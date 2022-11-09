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
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.parser.DelayedSchemaObjectInterfaceStorage;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaType;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnion;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GqlSchemaUnionParserTest {

    @Test
    void canParseUnionTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getSingleUnion();
        Assertions.assertTrue(parser.canParse(unionType));
    }

    @Test
    void cannotParseNotUnionTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String notUnionType = GraphQlSchemaGenerator.getSimpleType();
        Assertions.assertFalse(parser.canParse(notUnionType));
    }

    @Test
    void parseMultiUnionTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        SchemaObject schemaObject1 = new GqlSchemaType("com.test", "SimpleType", new ArrayList<>(),
                Collections.emptyList());
        when(schemaObjectStorage.get(eq("SimpleType"))).thenReturn(schemaObject1);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getMultiUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        verify(schemaObjectStorage).get(eq("SimpleType"));
        when(schemaObjectStorage.get(eq("MultiUnion"))).thenReturn(schemaUnion);
        Assertions.assertEquals(1, schemaObject1.getInterfaces().size());
        Assertions.assertEquals(schemaUnion, schemaObject1.getInterfaces().iterator().next());
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("MultiUnion", schemaUnion.getName());
        Assertions.assertEquals("com.test", schemaUnion.getPackage());
        Assertions.assertNotNull(schemaUnion.getFields());
        Assertions.assertTrue(schemaUnion.getFields().isEmpty());
        Assertions.assertNotNull(schemaUnion.getInterfaces());
        Assertions.assertTrue(schemaUnion.getInterfaces().isEmpty());
        Assertions.assertNotNull(schemaUnion.getUnionTypes());
        Assertions.assertEquals(2, schemaUnion.getUnionTypes().length);
        SchemaObject schemaObject2 = Mockito.mock(SchemaObject.class);
        when(schemaObjectStorage.get(eq("ComplexType"))).thenReturn(schemaObject2);
        Assertions.assertEquals("simple_type", schemaUnion.getUnionTypes()[0].getRawSchemaName());
        Assertions.assertEquals(schemaObject1, schemaUnion.getUnionTypes()[0].getType());
        Assertions.assertEquals("ComplexType", schemaUnion.getUnionTypes()[1].getRawSchemaName());
        Assertions.assertEquals(schemaObject2, schemaUnion.getUnionTypes()[1].getType());
    }

    @Test
    void parseSingleUnionTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getSingleUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("SingleUnion", schemaUnion.getName());
        Assertions.assertEquals("com.test", schemaUnion.getPackage());
        Assertions.assertNull(schemaUnion.getSuperclass());
        Assertions.assertNotNull(schemaUnion.getFields());
        Assertions.assertTrue(schemaUnion.getFields().isEmpty());
        Assertions.assertNotNull(schemaUnion.getInterfaces());
        Assertions.assertTrue(schemaUnion.getInterfaces().isEmpty());
        Assertions.assertNotNull(schemaUnion.getUnionTypes());
        Assertions.assertEquals(1, schemaUnion.getUnionTypes().length);
        SchemaObject schemaObject = new GqlSchemaType("com.test", "SimpleType", new ArrayList<>(),
                Collections.emptyList());
        when(schemaObjectStorage.get(eq("SimpleType"))).thenReturn(schemaObject);
        Assertions.assertEquals("simple_type", schemaUnion.getUnionTypes()[0].getRawSchemaName());
        Assertions.assertEquals(schemaObject, schemaUnion.getUnionTypes()[0].getType());
    }

    @Test
    void parseUnionTypeAddsPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getMultiUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("MultiUnionPojo", schemaUnion.getName());
        verify(schemaObjectStorage).get(eq("SimpleTypePojo"));
        verify(schemaObjectStorage).get(eq("ComplexTypePojo"));
    }

    @Test
    void parseUnionTypeAddsPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getSingleUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("TestSingleUnionPojo", schemaUnion.getName());
        verify(schemaObjectStorage).get(eq("TestSimpleTypePojo"));
    }

    @Test
    void parseUnionTypeAddsPrefixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getSingleUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("TestSingleUnion", schemaUnion.getName());
        verify(schemaObjectStorage).get(eq("TestSimpleType"));
    }

    @Test
    void parseUnionTypeNotUpdateNamesToJavaStyleAddPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getMultiUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("Testmulti_unionPojo", schemaUnion.getName());
        verify(schemaObjectStorage).get(eq("Testsimple_typePojo"));
        verify(schemaObjectStorage).get(eq("TestComplexTypePojo"));
    }

    @Test
    void parseUnionTypeNotUpdateNamesToJavaStyleTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String unionType = GraphQlSchemaGenerator.getMultiUnion();
        GqlSchemaUnion schemaUnion = parser.parse(unionType);
        Assertions.assertNotNull(schemaUnion);
        Assertions.assertEquals("multi_union", schemaUnion.getName());
        verify(schemaObjectStorage).get(eq("simple_type"));
        verify(schemaObjectStorage).get(eq("ComplexType"));
    }

    @Test
    void parseUnsupportedEntityTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaUnionParser parser = new GqlSchemaUnionParser(pluginExtension, schemaObjectStorage, interfaceStorage);
        String notUnionType = GraphQlSchemaGenerator.getSimpleType();
        GqlEntityParsingException exception = Assertions.assertThrows(GqlEntityParsingException.class,
                () -> parser.parse(notUnionType));
        Assertions.assertEquals("Cannot parse to union entity: " + notUnionType, exception.getMessage());
    }
}
