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
package com.github.vladislavsevruk.generator.model.graphql.generator;

import com.github.vladislavsevruk.generator.java.picker.ClassContentGeneratorPicker;
import com.github.vladislavsevruk.generator.java.picker.ClassContentGeneratorPickerImpl;
import com.github.vladislavsevruk.generator.java.provider.ClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.java.storage.ClassContentGeneratorProviderStorage;
import com.github.vladislavsevruk.generator.java.storage.ClassContentGeneratorProviderStorageImpl;
import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnion;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnionType;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class GqlModelGeneratorTest {

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @Test
    void generateTest() {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        String targetDirPath = TestData.tempTestRssPath("generateTest" + File.separator);
        SchemaObjectStorage storage = Mockito.mock(SchemaObjectStorage.class);
        GqlSchemaField schemaField11 = new GqlSchemaField("field11", "field11", CommonJavaSchemaEntity.BOOLEAN, false);
        SchemaObject schemaObject1 = new GqlSchemaObject("com.test", "TestObject1", null, Collections.emptyList(),
                Collections.singletonList(schemaField11));
        SchemaObject schemaObject2 = new GqlSchemaUnion("com.test", "TestObject2",
                new GqlSchemaUnionType[] { new GqlSchemaUnionType("TestObject2", schemaObject1) });
        GqlSchemaField schemaField31 = new GqlSchemaField("field31", "field_31", schemaObject1, true);
        GqlSchemaField schemaField32 = new GqlSchemaField("field32", "field32", new ArraySchemaEntity(schemaObject1),
                true);
        SchemaObject schemaObject3 = new GqlSchemaObject("com.test", "TestObject3", null,
                Collections.singletonList(schemaObject2), Arrays.asList(schemaField31, schemaField32));
        Collection<SchemaObject> schemaObjects = Arrays.asList(schemaObject2, schemaObject1, schemaObject3);
        Mockito.when(storage.getAllObjects()).thenReturn(schemaObjects);
        ClassContentGeneratorPicker picker = setupClassContentGeneratorPicker();
        new GqlModelGenerator(picker).generate(extension, targetDirPath, storage);
        String packageDirPath = String.format("%sgenerated%ssources%<sgraphqlModels%<sjava%<smain%<scom%<stest%<s",
                targetDirPath, File.separator);
        File packageDir = new File(packageDirPath);
        Assertions.assertTrue(packageDir.exists());
        Assertions.assertTrue(packageDir.isDirectory());
        String[] fileNames = packageDir.list();
        Assertions.assertNotNull(fileNames);
        Assertions.assertEquals(schemaObjects.size(), fileNames.length);
        List<String> classNames = Arrays.asList(fileNames);
        verifyGeneratedFile1(packageDirPath, classNames);
        verifyGeneratedFile2(packageDirPath, classNames);
        verifyGeneratedFile3(packageDirPath, classNames);
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }

    private List<String> nonEmptyLines(String filePath) {
        String fileContent = ReadWriteFileUtil.readFileContent(filePath);
        Assertions.assertNotNull(fileContent);
        return Arrays.asList(fileContent.split("\\s*\\n+\\s*"));
    }

    private ClassContentGeneratorPicker setupClassContentGeneratorPicker() {
        ClassContentGeneratorProviderStorage generatorProviderStorage = new ClassContentGeneratorProviderStorageImpl();
        generatorProviderStorage.addBefore(new GqlModelClassContentGeneratorProvider(),
                ClassContentGeneratorProvider.class);
        return new ClassContentGeneratorPickerImpl(generatorProviderStorage);
    }

    private void verifyGeneratedFile1(String packageDirPath, List<String> classNames) {
        String fileName = "TestObject1.java";
        Assertions.assertTrue(classNames.contains(fileName));
        List<String> nonEmptyLines = nonEmptyLines(packageDirPath + fileName);
        Assertions.assertEquals("package com.test;", nonEmptyLines.get(1));
        Assertions.assertEquals("import com.fasterxml.jackson.annotation.JsonProperty;", nonEmptyLines.get(2));
        Assertions.assertEquals("import com.github.vladislavsevruk.generator.annotation.GqlField;",
                nonEmptyLines.get(3));
        Assertions.assertEquals("import lombok.Data;", nonEmptyLines.get(4));
        Assertions.assertEquals("import lombok.experimental.Accessors;", nonEmptyLines.get(5));
        Assertions.assertEquals("@Accessors(chain = true)", nonEmptyLines.get(6));
        Assertions.assertEquals("@Data", nonEmptyLines.get(7));
        Assertions.assertEquals("public class TestObject1 {", nonEmptyLines.get(8));
        Assertions.assertEquals("@JsonProperty(\"field11\")", nonEmptyLines.get(9));
        Assertions.assertEquals("@GqlField", nonEmptyLines.get(10));
        Assertions.assertEquals("private Boolean field11;", nonEmptyLines.get(11));
        Assertions.assertEquals("}", nonEmptyLines.get(12));
    }

    private void verifyGeneratedFile2(String packageDirPath, List<String> classNames) {
        String fileName = "TestObject2.java";
        Assertions.assertTrue(classNames.contains(fileName));
        List<String> nonEmptyLines = nonEmptyLines(packageDirPath + fileName);
        Assertions.assertEquals("package com.test;", nonEmptyLines.get(1));
        Assertions.assertEquals("public interface TestObject2 {", nonEmptyLines.get(2));
        Assertions.assertEquals("}", nonEmptyLines.get(3));
    }

    private void verifyGeneratedFile3(String packageDirPath, List<String> classNames) {
        String fileName = "TestObject3.java";
        Assertions.assertTrue(classNames.contains(fileName));
        List<String> nonEmptyLines = nonEmptyLines(packageDirPath + fileName);
        Assertions.assertEquals("package com.test;", nonEmptyLines.get(1));
        Assertions.assertEquals("import com.fasterxml.jackson.annotation.JsonProperty;", nonEmptyLines.get(2));
        Assertions.assertEquals("import com.github.vladislavsevruk.generator.annotation.GqlField;",
                nonEmptyLines.get(3));
        Assertions.assertEquals("import lombok.Data;", nonEmptyLines.get(4));
        Assertions.assertEquals("import lombok.experimental.Accessors;", nonEmptyLines.get(5));
        Assertions.assertEquals("@Accessors(chain = true)", nonEmptyLines.get(6));
        Assertions.assertEquals("@Data", nonEmptyLines.get(7));
        Assertions.assertEquals("public class TestObject3 implements TestObject2 {", nonEmptyLines.get(8));
        Assertions.assertEquals("@JsonProperty(\"field_31\")", nonEmptyLines.get(9));
        Assertions.assertEquals("@GqlField(nonNull = true, withSelectionSet = true, name = \"field_31\")",
                nonEmptyLines.get(10));
        Assertions.assertEquals("private TestObject1 field31;", nonEmptyLines.get(11));
        Assertions.assertEquals("@JsonProperty(\"field32\")", nonEmptyLines.get(12));
        Assertions.assertEquals("@GqlField(nonNull = true, withSelectionSet = true)", nonEmptyLines.get(13));
        Assertions.assertEquals("private TestObject1[] field32;", nonEmptyLines.get(14));
        Assertions.assertEquals("}", nonEmptyLines.get(15));
    }
}
