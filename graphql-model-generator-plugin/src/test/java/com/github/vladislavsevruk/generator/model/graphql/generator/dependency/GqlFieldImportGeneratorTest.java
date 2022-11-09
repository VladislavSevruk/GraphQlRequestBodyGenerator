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
package com.github.vladislavsevruk.generator.model.graphql.generator.dependency;

import com.github.vladislavsevruk.generator.java.config.JavaClassGeneratorConfig;
import com.github.vladislavsevruk.generator.java.type.BaseSchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnion;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnionType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GqlFieldImportGeneratorTest {

    private static final String GQL_FIELD_IMPORT_STRING
            = "import com.github.vladislavsevruk.generator.annotation.GqlField;\n";

    @Test
    void generateForSchemaWithGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", Mockito.mock(SchemaEntity.class), false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsImportLine(config, schemaObject);
    }

    @Test
    void generateForSchemaWithGqlUnionTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsNoImportLines(config, schemaObject);
    }

    @Test
    void generateForSchemaWithMatchingAndNonMatchingFieldsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field1 = new GqlSchemaField("testName1", "testName1", union, false);
        SchemaField field2 = new GqlSchemaField("testName2", "testName2", union, true);
        SchemaField field3 = new GqlSchemaField("testName3", "rawTestName3", union, false);
        SchemaField field4 = new GqlSchemaField("testName4", "testName4", union, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Arrays.asList(field1, field2, field3, field4));
        verifyThereIsImportLine(config, schemaObject);
    }

    @Test
    void generateForSchemaWithNonGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new BaseSchemaField("testName", Mockito.mock(SchemaEntity.class));
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsNoImportLines(config, schemaObject);
    }

    @Test
    void generateForSchemaWithNonNullGqlUnionTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "testName", union, true);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsImportLine(config, schemaObject);
    }

    @Test
    void generateForSchemaWithNonNullGqlUnionWithOverriddenNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "rawTestName", union, true);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsImportLine(config, schemaObject);
    }

    @Test
    void generateForSchemaWithOverriddenGqlUnionNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "rawTestName", union, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsImportLine(config, schemaObject);
    }

    @Test
    void generateForSchemaWithSeveralNonMatchingFieldsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field1 = new GqlSchemaField("testName1", "testName1", union, false);
        SchemaField field2 = new GqlSchemaField("testName2", "testName2", union, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Arrays.asList(field1, field2));
        verifyThereIsNoImportLines(config, schemaObject);
    }

    private void verifyThereIsImportLine(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        Collection<String> result = new GqlFieldImportGenerator().generate(config, schemaObject);
        assertEquals(1, result.size());
        assertEquals(GQL_FIELD_IMPORT_STRING, result.iterator().next());
    }

    private void verifyThereIsNoImportLines(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        Collection<String> result = new GqlFieldImportGenerator().generate(config, schemaObject);
        assertTrue(result.isEmpty());
    }
}
