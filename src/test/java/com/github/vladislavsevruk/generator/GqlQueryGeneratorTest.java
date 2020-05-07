/*
 * MIT License
 *
 * Copyright (c) 2020 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator;

import com.github.vladislavsevruk.generator.param.GqlArgument;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategyManager;
import com.github.vladislavsevruk.generator.strategy.picker.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class GqlQueryGeneratorTest {

    private FieldsPickingStrategy customFieldsPickingStrategy = field -> field.getName().startsWith("named");

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
    }

    @Test
    public void generateAllFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .allFields("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet("testGqlQueryName", TestModelWithAnnotations.class,
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet("testGqlQueryName", TestModelWithAnnotations.class,
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .withoutFieldsWithSelectionSet("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutFieldsWithSelectionSet("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField} collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedNonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} setEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .allFields("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy,
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){"
                + "customNamedEntity{customNamedField customNamedNonNullableField} customNamedField "
                + "customNamedNonNullableEntity{customNamedField customNamedNonNullableField} "
                + "customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy,
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy,
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){customNamedEntity{"
                + "customNamedField customNamedNonNullableField} customNamedField "
                + "customNamedNonNullableEntity{customNamedField customNamedNonNullableField} "
                + "customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy,
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedNonNullableField} customNamedField customNamedNonNullableEntity{customNamedField "
                + "customNamedNonNullableField} customNamedNonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .onlyId("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .onlyId("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyNonNullable(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyNonNullable(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){"
                + "customNamedNonNullableField nonNullableField customNamedNonNullableEntity{"
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .onlyNonNullable("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyNonNullable(TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 3);
        String result = GqlQueryGenerator
                .onlyNonNullable(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(argument:3){"
                + "customNamedNonNullableField nonNullableField customNamedNonNullableEntity{"
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{customNamedNonNullableField "
                + "nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithCustomNameAndArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithCustomNameAndArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = new GqlArgument<>("argument", 5);
        String result = GqlQueryGenerator
                .onlyNonNullable("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(argument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(argument:5){customNamedNonNullableField "
                + "nonNullableField customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} "
                + "nonNullableEntity{customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullableFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyNonNullable("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
