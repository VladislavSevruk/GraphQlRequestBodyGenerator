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

import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.mockito.Mockito.when;

class GqlSchemaEnumParserTest {

    @Test
    void canParseEnumTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getSimpleEnumType();
        Assertions.assertTrue(parser.canParse(enumType));
    }

    @Test
    void cannotParseNotEnumTypeTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String notEnumType = GraphQlSchemaGenerator.getSimpleType();
        Assertions.assertFalse(parser.canParse(notEnumType));
    }

    @Test
    void parseComplexEnumTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getComplexEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("ComplexEnumType", schemaEnum.getName());
        Assertions.assertEquals("com.test", schemaEnum.getPackage());
        Assertions.assertNull(schemaEnum.getSuperclass());
        Assertions.assertNotNull(schemaEnum.getInterfaces());
        Assertions.assertTrue(schemaEnum.getInterfaces().isEmpty());
        Assertions.assertNotNull(schemaEnum.getFields());
        Assertions.assertEquals(5, schemaEnum.getFields().size());
        Iterator<SchemaField> fieldsIterator = schemaEnum.getFields().iterator();
        SchemaField enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE1", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE2", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE3", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE_4", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE5", enumField.getName());
        Assertions.assertNull(enumField.getType());
    }

    @Test
    void parseEnumAddsPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getComplexEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("ComplexEnumTypePojo", schemaEnum.getName());
    }

    @Test
    void parseEnumAddsPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getComplexEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("TestComplexEnumTypePojo", schemaEnum.getName());
    }

    @Test
    void parseEnumAddsPrefixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getSimpleEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("TestSimpleEnumType", schemaEnum.getName());
    }

    @Test
    void parseEnumNotUpdateNamesToJavaStyleAddsPrefixAndPostfixTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        Property<String> prefixProperty = TestExtensionUtil.mockProperty("Test");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(prefixProperty);
        Property<String> postfixProperty = TestExtensionUtil.mockProperty("Pojo");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(postfixProperty);
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getSimpleEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("Testsimple_enum_typePojo", schemaEnum.getName());
    }

    @Test
    void parseEnumNotUpdateNamesToJavaStyleTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        Property<Boolean> updateNamesToJavaStyle = TestExtensionUtil.mockProperty(false);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getSimpleEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("simple_enum_type", schemaEnum.getName());
    }

    @Test
    void parseSimpleEnumTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String enumType = GraphQlSchemaGenerator.getSimpleEnumType();
        GqlSchemaEnum schemaEnum = parser.parse(enumType);
        Assertions.assertNotNull(schemaEnum);
        Assertions.assertEquals("SimpleEnumType", schemaEnum.getName());
        Assertions.assertEquals("com.test", schemaEnum.getPackage());
        Assertions.assertNotNull(schemaEnum.getInterfaces());
        Assertions.assertTrue(schemaEnum.getInterfaces().isEmpty());
        Assertions.assertNotNull(schemaEnum.getFields());
        Assertions.assertEquals(5, schemaEnum.getFields().size());
        Iterator<SchemaField> fieldsIterator = schemaEnum.getFields().iterator();
        SchemaField enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE1", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE2", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE3", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE_4", enumField.getName());
        Assertions.assertNull(enumField.getType());
        enumField = fieldsIterator.next();
        Assertions.assertEquals("VALUE5", enumField.getName());
        Assertions.assertNull(enumField.getType());
    }

    @Test
    void parseUnsupportedEntityTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        GqlSchemaEnumParser parser = new GqlSchemaEnumParser(pluginExtension);
        String notEnumType = GraphQlSchemaGenerator.getSimpleType();
        GqlEntityParsingException exception = Assertions.assertThrows(GqlEntityParsingException.class,
                () -> parser.parse(notEnumType));
        Assertions.assertEquals("Cannot parse to enum entity: " + notEnumType, exception.getMessage());
    }
}
