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
import com.github.vladislavsevruk.generator.java.type.SchemaUnit;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

class GqlSchemaParserImplTest {

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @Test
    void parseSchemaTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        String schemaFilePath = TestData.tempTestRssPath("parseSchemaTestSchema");
        Property<String> schemaFilePathProperty = TestExtensionUtil.mockProperty(schemaFilePath);
        when(extension.getPathToSchemaFile()).thenReturn(schemaFilePathProperty);
        String schema = new GraphQlSchemaGenerator().addComplexInput().addMultiUnion().build();
        ReadWriteFileUtil.replaceFileContent(schemaFilePath, schema);
        SchemaObjectStorage storage = new GqlSchemaParserImpl(extension).parseSchema();
        Assertions.assertNotNull(storage);
        Assertions.assertNotNull(storage.getAllObjects());
        Assertions.assertEquals(8, storage.getAllObjects().size());
        Set<String> objectNames = storage.getAllObjects().stream().map(SchemaUnit::getName).collect(Collectors.toSet());
        Assertions.assertFalse(objectNames.contains("Query"));
        Assertions.assertFalse(objectNames.contains("Mutation"));
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }
}
