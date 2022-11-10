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
package com.github.vladislavsevruk.generator.model.graphql.collector.impl;

import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SchemaEntityCollectorImplTest {

    @Test
    void collectComplexInputTest() {
        String schema = new GraphQlSchemaGenerator().addComplexInput().build();
        String schemaPath = TestData.tempTestRssPath("collectComplexInputTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(6, entities.size());
        // there's no need to check 'Query' and 'Mutation' type
        assertEquals(GraphQlSchemaGenerator.getComplexInput(), entities.get(2));
        assertEquals(GraphQlSchemaGenerator.getSimpleInput(), entities.get(3));
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(4));
        assertEquals(GraphQlSchemaGenerator.getComplexEnumType(), entities.get(5));
    }

    @Test
    void collectComplexTypeTest() {
        String schema = new GraphQlSchemaGenerator().addComplexType().build();
        String schemaPath = TestData.tempTestRssPath("collectComplexTypeTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(5, entities.size());
        // there's no need to check 'Query' type
        assertEquals(GraphQlSchemaGenerator.getComplexType(), entities.get(1));
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(2));
        assertEquals(GraphQlSchemaGenerator.getSimpleEnumType(), entities.get(3));
        assertEquals(GraphQlSchemaGenerator.getSingleUnion(), entities.get(4));
    }

    @Test
    void collectFromNonExistentFileTest() {
        String schemaPath = TestData.tempTestRssPath("collectFromNonExistentFileTestSchema");
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertTrue(entities.isEmpty());
    }

    @Test
    void collectMultiUnionTest() {
        String schema = new GraphQlSchemaGenerator().addMultiUnion().build();
        String schemaPath = TestData.tempTestRssPath("collectMultiUnionTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(6, entities.size());
        // there's no need to check 'Query' type
        assertEquals(GraphQlSchemaGenerator.getMultiUnion(), entities.get(1));
        assertEquals(GraphQlSchemaGenerator.getComplexType(), entities.get(2));
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(3));
        assertEquals(GraphQlSchemaGenerator.getSimpleEnumType(), entities.get(4));
        assertEquals(GraphQlSchemaGenerator.getSingleUnion(), entities.get(5));
    }

    @Test
    void collectSimpleInputTest() {
        String schema = new GraphQlSchemaGenerator().addSimpleInput().build();
        String schemaPath = TestData.tempTestRssPath("collectSimpleInputTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(4, entities.size());
        // there's no need to check 'Query' and 'Mutation' type
        assertEquals(GraphQlSchemaGenerator.getSimpleInput(), entities.get(2));
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(3));
    }

    @Test
    void collectSimpleTypeTest() {
        String schema = new GraphQlSchemaGenerator().addSimpleType().build();
        String schemaPath = TestData.tempTestRssPath("collectSimpleTypeTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(2, entities.size());
        // there's no need to check 'Query' type
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(1));
    }

    @Test
    void collectSingleUnionTest() {
        String schema = new GraphQlSchemaGenerator().addSingleUnion().build();
        String schemaPath = TestData.tempTestRssPath("collectSingleUnionTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(3, entities.size());
        // there's no need to check 'Query' type
        assertEquals(GraphQlSchemaGenerator.getSingleUnion(), entities.get(1));
        assertEquals(GraphQlSchemaGenerator.getSimpleType(), entities.get(2));
    }

    @Test
    void collectWithoutExtraWhitespaceCharactersTest() {
        String schema = removeExtraWhitespaceCharacters(new GraphQlSchemaGenerator().addMultiUnion().build());
        String schemaPath = TestData.tempTestRssPath("collectWithoutExtraWhitespaceCharactersTestSchema");
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        List<String> entities = new SchemaEntityCollectorImpl().collect(schemaPath);
        assertEquals(6, entities.size());
        // there's no need to check 'Query' type
        assertEquals(removeExtraWhitespaceCharacters(GraphQlSchemaGenerator.getMultiUnion()), entities.get(1));
        assertEquals(removeExtraWhitespaceCharacters(GraphQlSchemaGenerator.getComplexType()), entities.get(2));
        assertEquals(removeExtraWhitespaceCharacters(GraphQlSchemaGenerator.getSimpleType()), entities.get(3));
        assertEquals(removeExtraWhitespaceCharacters(GraphQlSchemaGenerator.getSimpleEnumType()), entities.get(4));
        assertEquals(removeExtraWhitespaceCharacters(GraphQlSchemaGenerator.getSingleUnion()), entities.get(5));
    }

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }

    private String removeExtraWhitespaceCharacters(String value) {
        return value.replaceAll("#[^\\n]+", "").replaceAll("\\n+", " ");//.replaceAll("\\s+", " ");
    }
}
