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

class GqlFieldAnnotationGeneratorTest {

    private static final String FIELD_ANNOTATION = "    @GqlField\n";
    private static final String FIELD_ANNOTATION_PATTERN = "    @GqlField(%s)\n";

    @Test
    void generateForBigDecimalGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", CommonJavaSchemaEntity.BIG_DECIMAL, false);
        verifyAnnotationWasGenerated(config, field);
    }

    @Test
    void generateForEnumGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "testName", schemaEnum, false);
        verifyAnnotationWasGenerated(config, field);
    }

    @Test
    void generateForGqlFieldWithOverriddenNameAndSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "rawTestName", object, false);
        verifyAnnotationWasGenerated(config, field, "withSelectionSet = true, name = \"rawTestName\"");
    }

    @Test
    void generateForGqlFieldWithOverriddenNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "rawTestName", CommonJavaSchemaEntity.BOOLEAN, false);
        verifyAnnotationWasGenerated(config, field, "name = \"rawTestName\"");
    }

    @Test
    void generateForGqlFieldWithSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "testName", object, false);
        verifyAnnotationWasGenerated(config, field, "withSelectionSet = true");
    }

    @Test
    void generateForGqlUnionWithOverriddenNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "rawTestName", union, false);
        verifyAnnotationWasGenerated(config, field, "name = \"rawTestName\"");
    }

    @Test
    void generateForIterableEnumGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaEnum schemaEnum = new GqlSchemaEnum("com.test", "TestEnum", Collections.emptyList());
        ArraySchemaEntity enumArraySchemaEntity = new ArraySchemaEntity(schemaEnum);
        SchemaField field = new GqlSchemaField("testName", "testName", enumArraySchemaEntity, false);
        verifyAnnotationWasGenerated(config, field);
    }

    @Test
    void generateForIterableGqlFieldWithSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SetSchemaEntity setSchemaEntity = new SetSchemaEntity(object);
        SchemaField field = new GqlSchemaField("testName", "testName", setSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field, "withSelectionSet = true");
    }

    @Test
    void generateForIterableScalarGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        ListSchemaEntity scalarListSchemaEntity = new ListSchemaEntity(CommonJavaSchemaEntity.BOOLEAN);
        SchemaField field = new GqlSchemaField("testName", "testName", scalarListSchemaEntity, false);
        verifyAnnotationWasGenerated(config, field);
    }

    @Test
    void generateForNonGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new BaseSchemaField("testName", Mockito.mock(SchemaEntity.class));
        verifyAnnotationWasNotGenerated(config, field);
    }

    @Test
    void generateForNonNullGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", CommonJavaSchemaEntity.BOOLEAN, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true");
    }

    @Test
    void generateForNonNullGqlFieldWithOverriddenNameAndSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "rawTestName", object, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true, withSelectionSet = true, name = \"rawTestName\"");
    }

    @Test
    void generateForNonNullGqlFieldWithOverriddenNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "rawTestName", CommonJavaSchemaEntity.BOOLEAN, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true, name = \"rawTestName\"");
    }

    @Test
    void generateForNonNullGqlFieldWithSelectionSetTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaObject object = new GqlSchemaObject("com.test", "TestObject", null, Collections.emptyList(),
                Collections.emptyList());
        SchemaField field = new GqlSchemaField("testName", "testName", object, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true, withSelectionSet = true");
    }

    @Test
    void generateForNonNullGqlUnionTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "testName", union, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true");
    }

    @Test
    void generateForNonNullGqlUnionWithOverriddenNameTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "rawTestName", union, true);
        verifyAnnotationWasGenerated(config, field, "nonNull = true, name = \"rawTestName\"");
    }

    @Test
    void generateForScalarGqlFieldTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        SchemaField field = new GqlSchemaField("testName", "testName", CommonJavaSchemaEntity.BOOLEAN, false);
        verifyAnnotationWasGenerated(config, field);
    }

    @Test
    void generateForSimpleGqlUnionTest() {
        JavaClassGeneratorConfig config = JavaClassGeneratorConfig.builder().build();
        GqlSchemaUnion union = new GqlSchemaUnion("com.test", "TestUnion", new GqlSchemaUnionType[0]);
        SchemaField field = new GqlSchemaField("testName", "testName", union, false);
        verifyAnnotationWasNotGenerated(config, field);
    }

    private void verifyAnnotationWasGenerated(JavaClassGeneratorConfig config, SchemaField field) {
        String result = new GqlFieldAnnotationGenerator().generate(config, field);
        Assertions.assertEquals(FIELD_ANNOTATION, result);
    }

    private void verifyAnnotationWasGenerated(JavaClassGeneratorConfig config, SchemaField field, String parameters) {
        String result = new GqlFieldAnnotationGenerator().generate(config, field);
        Assertions.assertEquals(String.format(FIELD_ANNOTATION_PATTERN, parameters), result);
    }

    private void verifyAnnotationWasNotGenerated(JavaClassGeneratorConfig config, SchemaField field) {
        String result = new GqlFieldAnnotationGenerator().generate(config, field);
        Assertions.assertEquals("", result);
    }
}
