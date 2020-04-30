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

import com.github.vladislavsevruk.generator.param.QueryArgument;
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
    public void generateAllFieldsExceptEntitiesAndIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName", TestModelWithAnnotations.class,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .withoutEntities(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptEntitiesAndIgnoredWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .withoutEntities(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .allFields("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .allFields(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} mandatoryEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id mandatoryField customNamedField customNamedMandatoryField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities(
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName", TestModelWithAnnotations.class,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.withoutEntities("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .withoutEntities(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptEntitiesWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .withoutEntities(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .allFields("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.allFields("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .allFields(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "mandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} customNamedEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .allFields(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField customNamedField "
                + "customNamedMandatoryField} mandatoryEntity{collectionField fieldWithFieldAnnotation idField id "
                + "mandatoryField customNamedField customNamedMandatoryField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{collectionField fieldWithFieldAnnotation idField id mandatoryField "
                + "customNamedField customNamedMandatoryField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id mandatoryField customNamedField customNamedMandatoryField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id mandatoryField customNamedField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy,
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){"
                + "customNamedEntity{customNamedField customNamedMandatoryField} customNamedField "
                + "customNamedMandatoryEntity{customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy,
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .customQuery("testGqlQueryName", TestModelWithAnnotations.class, customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.customQuery("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                customFieldsPickingStrategy);
        String expectedResult = "{\"query\":\"{testGqlQueryName{customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator.customQuery(TestModelWithAnnotations.class, customFieldsPickingStrategy,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){customNamedEntity{customNamedField "
                + "customNamedMandatoryField} customNamedField customNamedMandatoryEntity{customNamedField "
                + "customNamedMandatoryField} customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .customQuery(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        customFieldsPickingStrategy, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){customNamedEntity{"
                + "customNamedField customNamedMandatoryField} customNamedField "
                + "customNamedMandatoryEntity{customNamedField customNamedMandatoryField} "
                + "customNamedMandatoryField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .onlyId("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyMandatory(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName", TestModelWithAnnotations.class,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithCustomNameTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyMandatory(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMandatoryFieldsExceptIgnoredWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useAllExceptIgnoredFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyMandatory(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator
                .onlyId("testGqlQueryName", TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyId("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator.onlyId(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyId(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} mandatoryEntity{id} customNamedEntity{id} "
                + "customNamedMandatoryEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory(TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{customGqlQuery{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator
                .onlyMandatory(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations{mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithCustomNameAndQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithCustomNameAndQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 5);
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName", TestModelWithAnnotations.class,
                Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{testGqlQueryName(queryArgument:5){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithCustomNameTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName", TestModelWithAnnotations.class);
        String expectedResult = "{\"query\":\"{testGqlQueryName{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithCustomNameTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        String result = GqlQueryGenerator.onlyMandatory("testGqlQueryName",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {});
        String expectedResult = "{\"query\":\"{testGqlQueryName{mandatoryField customNamedMandatoryField "
                + "mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithQueryArgumentsTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyMandatory(TestModelWithAnnotations.class, Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{customGqlQuery(queryArgument:3){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedMandatoryFieldsWithQueryArgumentsTypeProviderTest() {
        FieldMarkingStrategyManager.useOnlyMarkedFieldsStrategy();
        QueryArgument<Integer> queryArgument = new QueryArgument<>("queryArgument", 3);
        String result = GqlQueryGenerator
                .onlyMandatory(new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                        Collections.singleton(queryArgument));
        String expectedResult = "{\"query\":\"{GenericTestModelWithAnnotations(queryArgument:3){mandatoryField "
                + "customNamedMandatoryField mandatoryEntity{mandatoryField customNamedMandatoryField} "
                + "customNamedMandatoryEntity{mandatoryField customNamedMandatoryField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
