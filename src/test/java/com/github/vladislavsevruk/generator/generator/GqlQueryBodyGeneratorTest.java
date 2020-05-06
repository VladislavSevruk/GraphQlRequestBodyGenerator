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
package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.param.QueryVariable;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.OnlyMarkedFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyIdFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyNonNullableFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.WithoutFieldsWithSelectionSetPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GqlQueryBodyGeneratorTest {

    @Test
    public void generateAllFieldsExceptIgnoredTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
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
    public void generateForGenericModelTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
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
    public void generateNonNullableExceptIgnoredTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
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
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithArrayAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<Short[]> arrayVariable = new QueryVariable<>("arrayVariable", new Short[]{ 1, 2 });
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), arrayVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(arrayVariable:[1,2]){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithIterableAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<List<Boolean>> listVariable = new QueryVariable<>("listVariable", Arrays.asList(true, false));
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), listVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(listVariable:[true,false]){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithNullAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<String> stringVariable = new QueryVariable<>("stringVariable", null);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), stringVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringVariable:null){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithSeveralVariablesTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        List<QueryVariable<?>> variables = new ArrayList<>(2);
        QueryVariable<Integer> firstVariable = new QueryVariable<>("firstVariable", 5);
        QueryVariable<Long> secondVariable = new QueryVariable<>("secondVariable", 15L);
        variables.add(firstVariable);
        variables.add(secondVariable);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), variables);
        String expectedResult = "{\"query\":\"{customGqlQuery(firstVariable:5,secondVariable:15){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithSingleVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<Integer> variable = new QueryVariable<>("intVariable", 5);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), variable);
        String expectedResult = "{\"query\":\"{customGqlQuery(intVariable:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithStringArrayAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<String[]> arrayVariable = new QueryVariable<>("stringArrayVariable",
                new String[]{ "string value 1", "string value 2" });
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), arrayVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringArrayVariable:[\\\"string value 1\\\","
                + "\\\"string value 2\\\"]){collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} "
                + "customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithStringAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<String> stringVariable = new QueryVariable<>("stringVariable", "string value");
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), stringVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringVariable:\\\"string value\\\"){"
                + "collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateQueryWithStringIterableAsVariableTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryVariable<List<String>> listVariable = new QueryVariable<>("stringListVariable",
                Arrays.asList("string value 1", "string value 2"));
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), listVariable);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringListVariable:[\\\"string value 1\\\","
                + "\\\"string value 2\\\"]){collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} "
                + "customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithCustomQueryNameTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("testCustomGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{testCustomGqlQuery{collectionEntity{collectionField "
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
    public void generateWithoutCustomQueryNameTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator(null, TestModelWithAnnotations.class,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
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
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
