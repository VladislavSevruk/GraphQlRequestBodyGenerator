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

import com.github.vladislavsevruk.generator.param.QueryArgument;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.OnlyMarkedFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyIdFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyNonNullableFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.WithoutEntitiesPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.InheritedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.InheritedTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithoutAnnotations;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GqlQueryBodyGeneratorTest {

    @Test
    public void buildAllExceptIgnoredGenericModelWithAnnotationsTest() {
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
    public void buildAllExceptIgnoredGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildAllExceptIgnoredInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField customNamedField customNamedNonNullableField nonNullableField "
                + "newEntityAtDescendant{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} newFieldAtDescendant "
                + "newFieldWithoutAnnotationAtDescendant collectionEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
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
    public void buildAllExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id newDelegateAtDescendant newEntityAtDescendant "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant newIgnoredFieldAtDescendant "
                + "collectionEntity collectionField fieldWithDelegateAnnotation fieldWithEntityAnnotation "
                + "fieldWithFieldAnnotation fieldWithIgnoreAnnotation fieldWithoutAnnotations idField listEntity "
                + "namedEntity namedField namedNonNullableEntity namedNonNullableField nonNullableEntity "
                + "nonNullableField queueEntity setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildAllExceptIgnoredModelWithAnnotationsTest() {
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
    public void buildAllExceptIgnoredModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildNonNullableExceptIgnoredModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyIdExceptIgnoredModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
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
    public void buildOnlyMarkedGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedIdModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id collectionField fieldWithFieldAnnotation idField "
                + "customNamedField customNamedNonNullableField nonNullableField newEntityAtDescendant{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "newFieldAtDescendant collectionEntity{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} fieldWithEntityAnnotation{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullableField "
                + "nonNullableField} listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
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
    public void buildOnlyMarkedInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedModelWithAnnotationsTest() {
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
    public void buildOnlyMarkedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedNonNullableModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id collectionField fieldWithFieldAnnotation idField "
                + "customNamedField customNamedNonNullableField nonNullableField newFieldAtDescendant}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildOnlyMarkedWithoutEntitiesModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithArrayAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<Short[]> arrayArgument = new QueryArgument<>("arrayArgument", new Short[]{ 1, 2 });
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), arrayArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(arrayArgument:[1,2]){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithIterableAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<List<Boolean>> listArgument = new QueryArgument<>("listArgument", Arrays.asList(true, false));
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), listArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(listArgument:[true,false]){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithNullAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<String> stringArgument = new QueryArgument<>("stringArgument", null);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), stringArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringArgument:null){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithSeveralArgumentsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        List<QueryArgument<?>> arguments = new ArrayList<>(2);
        QueryArgument<Integer> firstArgument = new QueryArgument<>("firstArgument", 5);
        QueryArgument<Long> secondArgument = new QueryArgument<>("secondArgument", 15L);
        arguments.add(firstArgument);
        arguments.add(secondArgument);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), arguments);
        String expectedResult = "{\"query\":\"{customGqlQuery(firstArgument:5,secondArgument:15){collectionEntity{id} "
                + "id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithSingleArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<Integer> argument = new QueryArgument<>("intArgument", 5);
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), argument);
        String expectedResult = "{\"query\":\"{customGqlQuery(intArgument:5){collectionEntity{id} id "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithStringArrayAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<String[]> arrayArgument = new QueryArgument<>("stringArrayArgument",
                new String[]{ "string value 1", "string value 2" });
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), arrayArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringArrayArgument:[\\\"string value 1\\\","
                + "\\\"string value 2\\\"]){collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} "
                + "customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithStringAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<String> stringArgument = new QueryArgument<>("stringArgument", "string value");
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), stringArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringArgument:\\\"string value\\\"){"
                + "collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildQueryWithStringIterableAsArgumentTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        QueryArgument<List<String>> listArgument = new QueryArgument<>("stringListArgument",
                Arrays.asList("string value 1", "string value 2"));
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy(), listArgument);
        String expectedResult = "{\"query\":\"{customGqlQuery(stringListArgument:[\\\"string value 1\\\","
                + "\\\"string value 2\\\"]){collectionEntity{id} id fieldWithEntityAnnotation{id} listEntity{id} "
                + "customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} queueEntity{id} "
                + "setEntity{id}}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredGenericModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredGenericModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {},
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredInheritedModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField customNamedField customNamedNonNullableField nonNullableField "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                InheritedTestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{id newDelegateAtDescendant newEntityAtDescendant "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant newIgnoredFieldAtDescendant "
                + "collectionEntity collectionField fieldWithDelegateAnnotation fieldWithEntityAnnotation "
                + "fieldWithFieldAnnotation fieldWithIgnoreAnnotation fieldWithoutAnnotations idField listEntity "
                + "namedEntity namedField namedNonNullableEntity namedNonNullableField nonNullableEntity "
                + "nonNullableField queueEntity setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredModelWithAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void buildWithoutEntitiesExceptIgnoredModelWithoutAnnotationsTest() {
        GqlQueryBodyGenerator bodyGenerator = new GqlQueryBodyGenerator("customGqlQuery",
                TestModelWithoutAnnotations.class, new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutEntitiesPickingStrategy());
        String expectedResult = "{\"query\":\"{customGqlQuery{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
