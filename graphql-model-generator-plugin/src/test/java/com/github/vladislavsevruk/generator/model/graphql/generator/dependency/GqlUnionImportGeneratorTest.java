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
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GqlUnionImportGeneratorTest {

    private static final String GQL_UNION_IMPORT_STRING
            = "import com.github.vladislavsevruk.generator.annotation.GqlUnion;\n";
    private static final String GQL_UNION_TYPE_IMPORT_STRING
            = "import com.github.vladislavsevruk.generator.annotation.GqlUnionType;\n";

    @Test
    void generateForSchemaWithGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", Mockito.mock(SchemaEntity.class), false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereIsNoImportLines(config, schemaObject);
    }

    @Test
    void generateForSchemaWithGqlUnionTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.singletonList(field));
        verifyThereAreImportLines(config, schemaObject);
    }

    @Test
    void generateForSchemaWithMatchingAndNonMatchingFieldsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestUnion", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field1 = new GqlSchemaField("testName1", "testName1", object, true);
        SchemaField field2 = new GqlSchemaField("testName2", "testName2", union, true);
        SchemaField field3 = new GqlSchemaField("testName3", "rawTestName3", union, false);
        SchemaField field4 = new GqlSchemaField("testName4", "testName4", object, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Arrays.asList(field1, field2, field3, field4));
        verifyThereAreImportLines(config, schemaObject);
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
        verifyThereAreImportLines(config, schemaObject);
    }

    @Test
    void generateForSchemaWithSeveralNonMatchingFieldsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestUnion", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field1 = new GqlSchemaField("testName1", "testName1", object, true);
        SchemaField field2 = new GqlSchemaField("testName2", "testName2", object, false);
        SchemaObject schemaObject = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Arrays.asList(field1, field2));
        verifyThereIsNoImportLines(config, schemaObject);
    }

    private void verifyThereAreImportLines(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        Collection<String> result = new GqlUnionImportGenerator().generate(config, schemaObject);
        assertEquals(2, result.size());
        Iterator<String> iterator = result.iterator();
        assertEquals(GQL_UNION_IMPORT_STRING, iterator.next());
        assertEquals(GQL_UNION_TYPE_IMPORT_STRING, iterator.next());
    }

    private void verifyThereIsNoImportLines(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        Collection<String> result = new GqlUnionImportGenerator().generate(config, schemaObject);
        assertTrue(result.isEmpty());
    }
}
