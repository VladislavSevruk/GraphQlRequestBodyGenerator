/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Uladzislau Seuruk
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
import com.github.vladislavsevruk.generator.param.GqlDelegateArgument;
import com.github.vladislavsevruk.generator.strategy.looping.EndlessLoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.looping.LoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.SelectionSetGenerationStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModel;
import com.github.vladislavsevruk.generator.test.data.InheritedInputTestModel;
import com.github.vladislavsevruk.generator.test.data.InputWithVariableFieldTestModel;
import com.github.vladislavsevruk.generator.test.data.NestedTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import com.github.vladislavsevruk.generator.test.data.TestEnum;
import com.github.vladislavsevruk.generator.test.data.TestModel;
import com.github.vladislavsevruk.generator.test.data.loop.LongLoopedItem1;
import com.github.vladislavsevruk.generator.test.data.loop.ShortLoopedItem1;
import com.github.vladislavsevruk.generator.test.data.loop.union.LongLoopedUnionItem1;
import com.github.vladislavsevruk.generator.test.data.loop.union.ShortLoopedUnionItem1;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GqlQueryRequestBodyGeneratorTest {

    private final FieldsPickingStrategy customFieldsPickingStrategy = field -> field.getName().startsWith("named");

    @AfterAll
    static void setInitialAutoContextRefresh() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
    }

    @Test
    void generateAllFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField entity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAlias:entityWithAlias{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument("
                + "argumentForEntityWithAliasAndArgument:\\\"valueForEntityWithAliasAndArgument\\\"){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} entityWithArgument(argumentForEntityWithArgument:"
                + "\\\"valueForEntityWithArgument\\\"){collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField listEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedNonNullEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} setEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField entity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAlias:entityWithAlias{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument("
                + "argumentForEntityWithAliasAndArgument:\\\"valueForEntityWithAliasAndArgument\\\"){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} entityWithArgument(argumentForEntityWithArgument:"
                + "\\\"valueForEntityWithArgument\\\"){collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField listEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField "
                + "fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedNonNullEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "id customNamedField customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} setEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptSelectionSetsAndIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptSelectionSetsAndIgnoredTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllFieldsExceptSelectionSetsAndIgnoredWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsExceptSelectionSetsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsExceptSelectionSetsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsExceptSelectionSetsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.fieldsWithoutSelectionSets()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionField aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"test value\\\") fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField entity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAlias:entityWithAlias{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\\\"valueForEntityWithAliasAndArgument\\\"){collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} entityWithArgument("
                + "argumentForEntityWithArgument:\\\"valueForEntityWithArgument\\\"){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:"
                + "fieldWithAliasAndArguments(argumentForFieldWithAliasAndArguments1:1,"
                + "argumentForFieldWithAliasAndArguments2:2) fieldWithArgument(argumentForFieldWithArgument:"
                + "\\\"test value\\\") fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField entity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAlias:entityWithAlias{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\\\"valueForEntityWithAliasAndArgument\\\"){collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} entityWithArgument("
                + "argumentForEntityWithArgument:\\\"valueForEntityWithArgument\\\"){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\\\"valueForFieldWithArgument\\\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllMarkedFieldsWithArgumentsTypeProviderTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        TypeProvider<GenericTestModel<NestedTestModel>> typeProvider
                = new TypeProvider<GenericTestModel<NestedTestModel>>() {};
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(Collections.singleton(argument))
                .selectionSet(typeProvider, SelectionSetGenerationStrategy.allFields()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "collectionField aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:"
                + "fieldWithAliasAndArguments(argumentForFieldWithAliasAndArguments1:1,"
                + "argumentForFieldWithAliasAndArguments2:2) fieldWithArgument(argumentForFieldWithArgument:"
                + "\\\"test value\\\") fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField fieldWithEntityAnnotation{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} listEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateCustomQueryExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateCustomQueryExceptIgnoredTypeProviderTest() {
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
    void generateCustomQueryExceptIgnoredWithArgumentsTest() {
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
    void generateCustomQueryExceptIgnoredWithArgumentsTypeProviderTest() {
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
    void generateCustomQueryMarkedFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, customFieldsPickingStrategy).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedEntity{customNamedField "
                + "customNamedNonNullField} customNamedField customNamedNonNullEntity{customNamedField "
                + "customNamedNonNullField} customNamedNonNullField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateCustomQueryMarkedFieldsTypeProviderTest() {
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
    void generateCustomQueryMarkedFieldsWithArgumentsTest() {
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
    void generateCustomQueryMarkedFieldsWithArgumentsTypeProviderTest() {
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
    void generateLongLoopedItemsAtSelectionSetDefaultLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").selectionSet(LongLoopedItem1.class)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 longLoopedItem2{field2 "
                + "longLoopedItem3{field3}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateLongLoopedItemsAtSelectionSetDefaultLoopBreakingStrategyTypeProviderTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(new TypeProvider<LongLoopedItem1>() {}).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 longLoopedItem2{field2 "
                + "longLoopedItem3{field3}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateLongLoopedItemsAtSelectionSetExcludeFirstEntityLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(LongLoopedItem1.class, EndlessLoopBreakingStrategy.excludeFirstEntry()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 longLoopedItem2{field2 "
                + "longLoopedItem3{field3}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateLongLoopedItemsAtSelectionSetExcludeFirstEntityLoopBreakingStrategyTypeProviderTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(new TypeProvider<LongLoopedItem1>() {}, EndlessLoopBreakingStrategy.excludeFirstEntry())
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 longLoopedItem2{field2 "
                + "longLoopedItem3{field3}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateLongLoopedUnionItemsAtSelectionSetDefaultLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").selectionSet(LongLoopedUnionItem1.class)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 longLoopedItem2{... on LongLoopedUnionItem2{"
                + "field2 longLoopedItem3{... on LongLoopedUnionItem3{field3}}}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} entity{id} aliasForEntityWithAlias:"
                + "entityWithAlias{id} aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument("
                + "argumentForEntityWithAliasAndArgument:\\\"valueForEntityWithAliasAndArgument\\\"){id} "
                + "entityWithArgument(argumentForEntityWithArgument:\\\"valueForEntityWithArgument\\\"){id} id "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdFieldsExceptIgnoredTypeProviderTest() {
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
    void generateOnlyIdFieldsExceptIgnoredWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} entity{id} "
                + "aliasForEntityWithAlias:entityWithAlias{id} aliasForEntityWithAliasAndArgument:"
                + "entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\\\"valueForEntityWithAliasAndArgument\\\"){id} entityWithArgument(argumentForEntityWithArgument:"
                + "\\\"valueForEntityWithArgument\\\"){id} id listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
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
    void generateOnlyMarkedIdFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} entity{id} aliasForEntityWithAlias:"
                + "entityWithAlias{id} aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument("
                + "argumentForEntityWithAliasAndArgument:\\\"valueForEntityWithAliasAndArgument\\\"){id} "
                + "entityWithArgument(argumentForEntityWithArgument:\\\"valueForEntityWithArgument\\\"){id} id "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdFieldsTypeProviderTest() {
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
    void generateOnlyMarkedIdFieldsWithArgumentsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        GqlArgument<Integer> argument = GqlArgument.of("argument", 3);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyId()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:3){collectionEntity{id} entity{id} "
                + "aliasForEntityWithAlias:entityWithAlias{id} aliasForEntityWithAliasAndArgument:"
                + "entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\\\"valueForEntityWithAliasAndArgument\\\"){id} entityWithArgument(argumentForEntityWithArgument:"
                + "\\\"valueForEntityWithArgument\\\"){id} id listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdFieldsWithArgumentsTypeProviderTest() {
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
    void generateOnlyMarkedNonNullFieldsTest() {
        FieldMarkingStrategySourceManager.selectionSet().useOnlyMarkedFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedNonNullFieldsTypeProviderTest() {
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
    void generateOnlyMarkedNonNullFieldsWithArgumentsTest() {
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
    void generateOnlyMarkedNonNullFieldsWithArgumentsTypeProviderTest() {
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
    void generateOnlyNonNullFieldsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(TestModel.class, SelectionSetGenerationStrategy.onlyNonNull()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyNonNullFieldsExceptIgnoredTypeProviderTest() {
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
    void generateOnlyNonNullFieldsExceptIgnoredWithArgumentsTest() {
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
    void generateOnlyNonNullFieldsExceptIgnoredWithArgumentsTypeProviderTest() {
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
    void generateShortLoopedItemsAtSelectionSetDefaultLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").selectionSet(ShortLoopedItem1.class)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 shortLoopedItem2{field2 shortLoopedItem1{field1 "
                + "shortLoopedItem2{field2 shortLoopedItem3{field3}} shortLoopedItem3{field3}} "
                + "shortLoopedItem3{field3}} shortLoopedItem3{field3}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateShortLoopedItemsAtSelectionSetDefaultLoopBreakingStrategyTypeProviderTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(new TypeProvider<ShortLoopedItem1>() {}).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 shortLoopedItem2{field2 shortLoopedItem1{field1 "
                + "shortLoopedItem2{field2 shortLoopedItem3{field3}} shortLoopedItem3{field3}} "
                + "shortLoopedItem3{field3}} shortLoopedItem3{field3}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateShortLoopedItemsAtSelectionSetExcludeFirstEntityLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(ShortLoopedItem1.class, EndlessLoopBreakingStrategy.excludeFirstEntry()).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 shortLoopedItem2{field2 shortLoopedItem1{field1 "
                + "shortLoopedItem2{field2 shortLoopedItem3{field3}} shortLoopedItem3{field3}} "
                + "shortLoopedItem3{field3}} shortLoopedItem3{field3}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateShortLoopedItemsAtSelectionSetExcludeFirstEntityLoopBreakingStrategyTypeProviderTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(new TypeProvider<ShortLoopedItem1>() {}, EndlessLoopBreakingStrategy.excludeFirstEntry())
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 shortLoopedItem2{field2 shortLoopedItem1{field1 "
                + "shortLoopedItem2{field2 shortLoopedItem3{field3}} shortLoopedItem3{field3}} "
                + "shortLoopedItem3{field3}} shortLoopedItem3{field3}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateShortLoopedUnionItemsAtSelectionSetDefaultLoopBreakingStrategyTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").selectionSet(ShortLoopedUnionItem1.class)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{field1 shortLoopedItems{... on ShortLoopedUnionItem2{"
                + "field2 shortLoopedItem1{... on ShortLoopedUnionItem1{field1 shortLoopedItems{"
                + "... on ShortLoopedUnionItem2{field2 shortLoopedItem3{... on ShortLoopedUnionItem3{field3}}} "
                + "... on ShortLoopedUnionItem3{field3}}}} shortLoopedItem3{... on ShortLoopedUnionItem3{field3}}} "
                + "... on ShortLoopedUnionItem3{field3 shortLoopedItem2{... on ShortLoopedUnionItem2{field2 "
                + "shortLoopedItem1{... on ShortLoopedUnionItem1{field1 shortLoopedItems{... on ShortLoopedUnionItem2{"
                + "field2}}}}}}}}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithArrayArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<String[]> argument = GqlArgument.of("argument", new String[]{ "1", "2" });
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[\\\"1\\\",\\\"2\\\"]){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithEnumArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<TestEnum> argument = GqlArgument.of("argument", TestEnum.TEST_VALUE_1);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:TEST_VALUE_1){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithEnumAsArrayArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<List<TestEnum>> argument = GqlArgument
                .of("argument", Arrays.asList(TestEnum.TEST_VALUE_1, TestEnum.TEST_VALUE_2));
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[TEST_VALUE_1,TEST_VALUE_2])"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithEnumAsIterableArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<TestEnum[]> argument = GqlArgument
                .of("argument", new TestEnum[]{ TestEnum.TEST_VALUE_1, TestEnum.TEST_VALUE_2 });
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[TEST_VALUE_1,TEST_VALUE_2])"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithIterableArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<List<Integer>> argument = GqlArgument.of("argument", Arrays.asList(1, 2));
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:[1,2]){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNullArgumentTest() {
        FieldMarkingStrategySourceManager.selectionSet().useAllExceptIgnoredFieldsStrategy();
        GqlArgument<String> argument = GqlArgument.of("argument", null);
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery").arguments(argument)
                .selectionSet(new TypeProvider<SimpleSelectionSetTestModel>() {}).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(argument:null){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNullStrategiesTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(SimpleSelectionSetTestModel.class, null, (LoopBreakingStrategy) null).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithoutSelectionSetTest() {
        GqlQueryRequestBodyGenerator queryRequestBodyGenerator = new GqlQueryRequestBodyGenerator("customGqlQuery");
        Assertions.assertThrows(NullPointerException.class, queryRequestBodyGenerator::generate);
    }

    @Test
    void generateWithoutSelectionSetTypeMetaTest() {
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .selectionSet(new TypeProvider<SimpleSelectionSetTestModel>() {}, null, (LoopBreakingStrategy) null)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithDelegateArgumentTest() {
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .arguments(GqlDelegateArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(subClassField:"
                + "\\\"subClassFieldValue\\\",testField:\\\"testFieldValue\\\")"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithDelegateArgumentWithVariablesTest() {
        InputWithVariableFieldTestModel inputModel = new InputWithVariableFieldTestModel()
                .setTestField("testField").setTestFieldWithAnnotationValues("testFieldWithAnnotationValues");
        inputModel.setTestField("testFieldValue");
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .arguments(GqlDelegateArgument.of(inputModel, true))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"variables\":{\"testField\":\"getTestField method\",\"variableMethodName\":"
                + "\"getVariableTypeInputMethod\"},\"query\":\"query($testField:String,$variableMethodName:"
                + "CustomType=\\\"test\\\"){customGqlQuery(testField:$testField,"
                + "testFieldWithAnnotationValues:\\\"testFieldWithAnnotationValues\\\","
                + "variableTypeInputMethod:$variableMethodName){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithDelegateArgumentWithoutVariablesTest() {
        InputWithVariableFieldTestModel inputModel = new InputWithVariableFieldTestModel()
                .setTestField("testField").setTestFieldWithAnnotationValues("testFieldWithAnnotationValues");
        inputModel.setTestField("testFieldValue");
        String result = new GqlQueryRequestBodyGenerator("customGqlQuery")
                .arguments(GqlDelegateArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery(testField:\\\"getTestField method\\\","
                + "testFieldWithAnnotationValues:\\\"testFieldWithAnnotationValues\\\","
                + "variableTypeInputMethod:\\\"getVariableTypeInputMethod\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
