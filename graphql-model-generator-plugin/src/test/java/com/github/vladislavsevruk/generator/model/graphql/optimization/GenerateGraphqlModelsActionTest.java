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

import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import com.github.vladislavsevruk.generator.model.graphql.test.data.GraphQlSchemaGenerator;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

class GenerateGraphqlModelsActionTest {

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @Test
    void executeTest() {
        Task task = Mockito.mock(Task.class);
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        String targetDirPath = TestData.tempTestRssPath("executeTest" + File.separator);
        String schemaPath = targetDirPath + "schema";
        Property<String> schemaPathProperty = TestExtensionUtil.mockProperty(schemaPath);
        when(extension.getPathToSchemaFile()).thenReturn(schemaPathProperty);
        String schema = new GraphQlSchemaGenerator().addMultiUnion().addComplexInput().build();
        ReadWriteFileUtil.replaceFileContent(schemaPath, schema);
        new GenerateGraphqlModelsAction<>(extension, targetDirPath).execute(task);
        String packageDirPath = String.format("%sgenerated%ssources%<sgraphqlModels%<sjava%<smain%<scom%<stest%<s",
                targetDirPath, File.separator);
        File packageDir = new File(packageDirPath);
        Assertions.assertTrue(packageDir.exists());
        Assertions.assertTrue(packageDir.isDirectory());
        String[] fileNames = packageDir.list();
        Assertions.assertNotNull(fileNames);
        Assertions.assertEquals(8, fileNames.length);
        List<String> classNames = Arrays.asList(fileNames);
        Assertions.assertTrue(classNames.contains("ComplexEnumType.java"));
        Assertions.assertTrue(classNames.contains("ComplexInput.java"));
        Assertions.assertTrue(classNames.contains("ComplexType.java"));
        Assertions.assertTrue(classNames.contains("MultiUnion.java"));
        Assertions.assertTrue(classNames.contains("SimpleEnumType.java"));
        Assertions.assertTrue(classNames.contains("SimpleInput.java"));
        Assertions.assertTrue(classNames.contains("SimpleType.java"));
        Assertions.assertTrue(classNames.contains("SingleUnion.java"));
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }
}
