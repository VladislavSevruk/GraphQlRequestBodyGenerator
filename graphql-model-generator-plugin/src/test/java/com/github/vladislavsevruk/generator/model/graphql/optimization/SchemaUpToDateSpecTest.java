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
package com.github.vladislavsevruk.generator.model.graphql.optimization;

import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class SchemaUpToDateSpecTest {

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @Test
    void isNotSatisfiedByNoPreviousHashSumTest() {
        String targetPath = TestData.tempTestRssPath("isSatisfiedByNoPreviousHashSumTest" + File.separator);
        Property<String> pathToSchemaFileProperty = Mockito.mock(Property.class);
        String schemaPath = targetPath + "testSchema";
        when(pathToSchemaFileProperty.get()).thenReturn(schemaPath);
        String schema = new GraphQlSchemaGenerator().addSimpleType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Task task = Mockito.mock(Task.class);
        boolean result = new SchemaUpToDateSpec<>(pathToSchemaFileProperty, targetPath).isSatisfiedBy(task);
        Assertions.assertFalse(result);
    }

    @Test
    void isNotSatisfiedByNoSchemaFileTest() {
        String targetPath = TestData.tempTestRssPath("isNotSatisfiedByNoSchemaFileTest" + File.separator);
        Property<String> pathToSchemaFileProperty = Mockito.mock(Property.class);
        String schemaPath = targetPath + "testSchema";
        when(pathToSchemaFileProperty.get()).thenReturn(schemaPath);
        Task task = Mockito.mock(Task.class);
        SchemaUpToDateSpec<Task> spec = new SchemaUpToDateSpec<>(pathToSchemaFileProperty, targetPath);
        spec.isSatisfiedBy(task);
        Assertions.assertFalse(spec.isSatisfiedBy(task));
    }

    @Test
    void isNotSatisfiedByNotSameAsPreviousHashSumTest() {
        String targetPath = TestData.tempTestRssPath("isNotSatisfiedByNotSameAsPreviousHashSumTest" + File.separator);
        Property<String> pathToSchemaFileProperty = Mockito.mock(Property.class);
        String schemaPath = targetPath + "testSchema";
        when(pathToSchemaFileProperty.get()).thenReturn(schemaPath);
        String schema = new GraphQlSchemaGenerator().addSimpleType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Task task = Mockito.mock(Task.class);
        SchemaUpToDateSpec<Task> spec = new SchemaUpToDateSpec<>(pathToSchemaFileProperty, targetPath);
        spec.isSatisfiedBy(task);
        schema = new GraphQlSchemaGenerator().addComplexType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Assertions.assertFalse(spec.isSatisfiedBy(task));
    }

    @Test
    void isSatisfiedBySameAsPreviousButNotAsInitialHashSumTest() {
        String targetPath = TestData.tempTestRssPath(
                "isSatisfiedByNotSameAsPreviousButNotAsInitialHashSumTest" + File.separator);
        Property<String> pathToSchemaFileProperty = Mockito.mock(Property.class);
        String schemaPath = targetPath + "testSchema";
        when(pathToSchemaFileProperty.get()).thenReturn(schemaPath);
        String schema = new GraphQlSchemaGenerator().addSimpleType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Task task = Mockito.mock(Task.class);
        SchemaUpToDateSpec<Task> spec = new SchemaUpToDateSpec<>(pathToSchemaFileProperty, targetPath);
        spec.isSatisfiedBy(task);
        schema = new GraphQlSchemaGenerator().addComplexType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Assertions.assertFalse(spec.isSatisfiedBy(task));
        Assertions.assertTrue(spec.isSatisfiedBy(task));
    }

    @Test
    void isSatisfiedBySameAsPreviousHashSumTest() {
        String targetPath = TestData.tempTestRssPath("isSatisfiedBySameAsPreviousHashSumTest" + File.separator);
        Property<String> pathToSchemaFileProperty = Mockito.mock(Property.class);
        String schemaPath = targetPath + "testSchema";
        when(pathToSchemaFileProperty.get()).thenReturn(schemaPath);
        String schema = new GraphQlSchemaGenerator().addSimpleType().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        Task task = Mockito.mock(Task.class);
        SchemaUpToDateSpec<Task> spec = new SchemaUpToDateSpec<>(pathToSchemaFileProperty, targetPath);
        spec.isSatisfiedBy(task);
        Assertions.assertTrue(spec.isSatisfiedBy(task));
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }
}
