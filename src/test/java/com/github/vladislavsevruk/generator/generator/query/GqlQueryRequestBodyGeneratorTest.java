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
package com.github.vladislavsevruk.generator.generator.query;

import com.github.vladislavsevruk.generator.param.GqlArgument;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.SelectionSetGenerationStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModel;
import com.github.vladislavsevruk.generator.test.data.NestedTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import com.github.vladislavsevruk.generator.test.data.TestModel;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GqlQueryRequestBodyGeneratorTest {

    private FieldsPickingStrategy customFieldsPickingStrategy = field -> field.getName().startsWith("named");

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
    }

    @Test
    public void generateAllFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} setEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField " + "nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField " + "nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField " + "nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField " + "nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField " + "nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllMarkedFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedNonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} setEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){"
                + "customNamedEntity{customNamedField customNamedNonNullField} customNamedField "
                + "customNamedNonNullEntity{customNamedField customNamedNonNullField} "
                + "customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateCustomQueryMarkedFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedEntity{"
                + "customNamedField customNamedNonNullField} customNamedField "
                + "customNamedNonNullEntity{customNamedField customNamedNonNullField} "
                + "customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullFieldsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField "
                + "nonNullField customNamedNonNullEntity{customNamedNonNullField nonNullField} "
                + "nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedNonNullField "
                + "nonNullField customNamedNonNullEntity{customNamedNonNullField nonNullField} "
                + "nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){"
                + "customNamedNonNullField nonNullField customNamedNonNullEntity{"
                + "customNamedNonNullField nonNullField} nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField "
                + "nonNullField customNamedNonNullEntity{customNamedNonNullField nonNullField} "
                + "nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){customNamedNonNullField "
                + "nonNullField customNamedNonNullEntity{customNamedNonNullField nonNullField} "
                + "nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyNonNullFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){"
                + "customNamedNonNullField nonNullField customNamedNonNullEntity{"
                + "customNamedNonNullField nonNullField} nonNullEntity{customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithArrayArgument() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer[]> argument = GqlArgument.of("argument", new Integer[]{ 1, 2 });
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[1,2]){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithIterableArgument() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<List<Integer>> argument = GqlArgument.of("argument", Arrays.asList(1, 2));
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[1,2]){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithNullArgument() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<String> argument = GqlArgument.of("argument", null);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(new TypeProvider<SimpleSelectionSetTestModel>() {}).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:null){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutSelectionSetTest() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new GqlQueryRequestBodyGenerator("customGqlQuery").generate());
    }
}
