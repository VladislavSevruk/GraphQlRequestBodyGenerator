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
package com.github.vladislavsevruk.generator.model.graphql.generator.field;

import com.github.vladislavsevruk.generator.java.config.JavaClassGeneratorConfig;
import com.github.vladislavsevruk.generator.java.type.BaseSchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.CollectionSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.IterableSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ListSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.SetSchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnion;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

class GqlUnionAnnotationGeneratorTest {

    private static final String UNION_ANNOTATION_PATTERN = "    @GqlUnion(%s)\n";

    @Test
    void generateForEnumGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "testName", schemaEnum, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForGqlFieldWithSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "testName", object, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForGqlUnionWithSeveralItemsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object1 = new GqlSchemaObject("com.test", "UnionType1", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaObject object2 = new GqlSchemaObject("com.test", "UnionType2", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType1 = new GqlSchemaUnionType("UnionType1", object1);
        GqlSchemaUnionType unionType2 = new GqlSchemaUnionType("UnionType2", object2);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion",
                new GqlSchemaUnionType[] { unionType1, unionType2 });
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        verifyAnnotationWasGenerated(config, field,
                "{ @GqlUnionType(UnionType1.class), @GqlUnionType(UnionType2.class) }");
    }

    @Test
    void generateForGqlUnionWithSeveralItemsWithNonMatchingNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object1 = new GqlSchemaObject("com.test", "TestObject1", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaObject object2 = new GqlSchemaObject("com.test", "TestObject2", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType1 = new GqlSchemaUnionType("UnionType1", object1);
        GqlSchemaUnionType unionType2 = new GqlSchemaUnionType("UnionType2", object2);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion",
                new GqlSchemaUnionType[] { unionType1, unionType2 });
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        verifyAnnotationWasGenerated(config, field,
                "{ @GqlUnionType(value = TestObject1.class, name = \"UnionType1\"), @GqlUnionType(value = TestObject2.class, name = \"UnionType2\") }");
    }

    @Test
    void generateForGqlUnionWithSingleItemTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "UnionType", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType = new GqlSchemaUnionType("UnionType", object);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[] { unionType });
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        verifyAnnotationWasGenerated(config, field, "@GqlUnionType(UnionType.class)");
    }

    @Test
    void generateForGqlUnionWithSingleItemWithNonMatchingNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType = new GqlSchemaUnionType("UnionType", object);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[] { unionType });
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        verifyAnnotationWasGenerated(config, field, "@GqlUnionType(value = TestObject.class, name = \"UnionType\")");
    }

    @Test
    void generateForIterableEnumGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        ArraySchemaEntity enumArraySchemaEntity = new ArraySchemaEntity(schemaEnum);
        SchemaField field = new GqlSchemaField("testName", "testName", enumArraySchemaEntity, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForIterableGqlFieldWithSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SetSchemaEntity setSchemaEntity = new SetSchemaEntity(object);
        SchemaField field = new GqlSchemaField("testName", "testName", setSchemaEntity, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForIterableGqlUnionWithSeveralItemsTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object1 = new GqlSchemaObject("com.test", "UnionType1", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaObject object2 = new GqlSchemaObject("com.test", "UnionType2", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType1 = new GqlSchemaUnionType("UnionType1", object1);
        GqlSchemaUnionType unionType2 = new GqlSchemaUnionType("UnionType2", object2);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion",
                new GqlSchemaUnionType[] { unionType1, unionType2 });
        IterableSchemaEntity iterableSchemaEntity = new IterableSchemaEntity(union);
        SchemaField field = new GqlSchemaField("testName", "testName", iterableSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field,
                "{ @GqlUnionType(UnionType1.class), @GqlUnionType(UnionType2.class) }");
    }

    @Test
    void generateForIterableGqlUnionWithSeveralItemsWithNonMatchingNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object1 = new GqlSchemaObject("com.test", "TestObject1", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaObject object2 = new GqlSchemaObject("com.test", "TestObject2", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType1 = new GqlSchemaUnionType("UnionType1", object1);
        GqlSchemaUnionType unionType2 = new GqlSchemaUnionType("UnionType2", object2);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion",
                new GqlSchemaUnionType[] { unionType1, unionType2 });
        CollectionSchemaEntity collectionSchemaEntity = new CollectionSchemaEntity(union);
        SchemaField field = new GqlSchemaField("testName", "testName", collectionSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field,
                "{ @GqlUnionType(value = TestObject1.class, name = \"UnionType1\"), @GqlUnionType(value = TestObject2.class, name = \"UnionType2\") }");
    }

    @Test
    void generateForIterableGqlUnionWithSingleItemTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "UnionType", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType = new GqlSchemaUnionType("UnionType", object);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[] { unionType });
        ListSchemaEntity listSchemaEntity = new ListSchemaEntity(union);
        SchemaField field = new GqlSchemaField("testName", "testName", listSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field, "@GqlUnionType(UnionType.class)");
    }

    @Test
    void generateForIterableGqlUnionWithSingleItemWithNoMatchingNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        GqlSchemaUnionType unionType = new GqlSchemaUnionType("UnionType", object);
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[] { unionType });
        SetSchemaEntity setSchemaEntity = new SetSchemaEntity(union);
        SchemaField field = new GqlSchemaField("testName", "testName", setSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field, "@GqlUnionType(value = TestObject.class, name = \"UnionType\")");
    }

    @Test
    void generateForIterableScalarGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        ListSchemaEntity scalarListSchemaEntity = new ListSchemaEntity(CommonJavaSchemaEntity.BOOLEAN);
        SchemaField field = new GqlSchemaField("testName", "testName", scalarListSchemaEntity, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForNonGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new BaseSchemaField("testName", Mockito.mock(SchemaEntity.class));
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForScalarGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", CommonJavaSchemaEntity.BOOLEAN, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    private void verifyAnnotationWasGenerated(JavaClassGeneratorConfig config, SchemaField field, String parameters) {
        String result = new GqlUnionAnnotationGenerator().generate(config, field);
        Assertions.assertEquals(String.format(UNION_ANNOTATION_PATTERN, parameters), result);
    }

    private void verifyAnnotationWasNotGenerated(JavaClassGeneratorConfig config, SchemaField field) {
        String result = new GqlUnionAnnotationGenerator().generate(config, field);
        Assertions.assertEquals("", result);
    }
}
