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

import com.github.vladislavsevruk.generator.strategy.looping.ExcludingLoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.looping.EndlessLoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.looping.LoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.OnlyMarkedFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.OnlyIdFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.OnlyNonNullFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.WithoutFieldsWithSelectionSetPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModel;
import com.github.vladislavsevruk.generator.test.data.InheritedTestModel;
import com.github.vladislavsevruk.generator.test.data.NestedTestModel;
import com.github.vladislavsevruk.generator.test.data.TestModel;
import com.github.vladislavsevruk.generator.test.data.loop.LongLoopedItem1;
import com.github.vladislavsevruk.generator.test.data.loop.SelfReferencedItem;
import com.github.vladislavsevruk.generator.test.data.loop.ShortLoopedItem1;
import com.github.vladislavsevruk.generator.test.data.union.TestModelWithUnion;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SelectionSetGeneratorTest {

    @Test
    void generateAllExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id customNamedField customNamedNonNullField nonNullField} collectionField "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithFieldAnnotation "
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
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "customNamedField customNamedNonNullField nonNullField newEntityAtDescendant{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} newFieldAtDescendant newFieldWithoutAnnotationAtDescendant "
                + "collectionEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithEntityAnnotation{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} listEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateAllExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id customNamedField customNamedNonNullField nonNullField} collectionField entity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAlias:entityWithAlias{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument("
                + "argumentForEntityWithAliasAndArgument:\"valueForEntityWithAliasAndArgument\"){collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} entityWithArgument(argumentForEntityWithArgument:"
                + "\"valueForEntityWithArgument\"){collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id customNamedField customNamedNonNullField nonNullField} aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"valueForFieldWithArgument\") "
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
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateNonNullExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateNonNullExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateNonNullExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyIdExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} entity{id} aliasForEntityWithAlias:entityWithAlias{id} "
                + "aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\"valueForEntityWithAliasAndArgument\"){id} entityWithArgument(argumentForEntityWithArgument:"
                + "\"valueForEntityWithArgument\"){id} id listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} collectionField aliasForFieldWithAlias:"
                + "fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} entity{id} aliasForEntityWithAlias:entityWithAlias{id} "
                + "aliasForEntityWithAliasAndArgument:entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\"valueForEntityWithAliasAndArgument\"){id} entityWithArgument(argumentForEntityWithArgument:"
                + "\"valueForEntityWithArgument\"){id} id listEntity{id} customNamedEntity{id} "
                + "customNamedNonNullEntity{id} nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedIdModelWithUnionsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithUnion.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{severalUnionTypes{... on UnionType1{id}} id singleUnionType{... on UnionType1{id}}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField customNamedField "
                + "customNamedNonNullField nonNullField newEntityAtDescendant{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} newFieldAtDescendant "
                + "collectionEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithEntityAnnotation{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} collectionField entity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForEntityWithAlias:entityWithAlias{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} aliasForEntityWithAliasAndArgument:"
                + "entityWithAliasAndArgument(argumentForEntityWithAliasAndArgument:"
                + "\"valueForEntityWithAliasAndArgument\"){collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} entityWithArgument("
                + "argumentForEntityWithArgument:\"valueForEntityWithArgument\"){collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "aliasForFieldWithAlias:fieldWithAlias aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"valueForFieldWithArgument\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField "
                + "listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} nonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation idField "
                + "id customNamedField customNamedNonNullField nonNullField} setEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedModelWithUnionsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithUnion.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{severalUnionTypes{... on UnionType1{id nonNullField} ... on UnionType{simpleField}} "
                + "id singleUnionType{... on UnionType1{id nonNullField}}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedNonNullGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedNonNullInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedNonNullModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedNonNullModelWithUnionsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithUnion.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{severalUnionTypes{... on UnionType1{id nonNullField}} id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedWithoutFieldsWithSelectionSetGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedWithoutFieldsWithSelectionSetInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField customNamedField "
                + "customNamedNonNullField nonNullField newFieldAtDescendant aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\")}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedWithoutFieldsWithSelectionSetModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"valueForFieldWithArgument\") "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateOnlyMarkedWithoutFieldsWithSelectionSetModelWithUnionsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithUnion.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta, new OnlyMarkedFieldMarkingStrategy(),
                new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategyLongLoopedItemTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(LongLoopedItem1.class);
        int nestingLevel = 1;
        LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), loopBreakingStrategy);
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{field1 longLoopedItem2{field2 longLoopedItem3{field3 longLoopedItem1{field1 "
                + "longLoopedItem2{field2 longLoopedItem3{field3}}}}}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategyShortLoopedItemTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(ShortLoopedItem1.class);
        int nestingLevel = 0;
        LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), loopBreakingStrategy);
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{field1 shortLoopedItem2{field2 shortLoopedItem3{field3 shortLoopedItem2{field2}}} "
                + "shortLoopedItem3{field3 shortLoopedItem2{field2}}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategySelfReferencedItemLevelZeroTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(SelfReferencedItem.class);
        int nestingLevel = 0;
        LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), loopBreakingStrategy);
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategySelfReferencedItemLevelOneTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(SelfReferencedItem.class);
        int nestingLevel = 1;
        LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), loopBreakingStrategy);
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id item{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategySelfReferencedItemLevelTwoTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(SelfReferencedItem.class);
        int nestingLevel = 2;
        LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), loopBreakingStrategy);
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id item{id item{id}}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithNestingLoopBreakingStrategySameAsWithDefaultOneTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(LongLoopedItem1.class);
        int nestingLevel = 0;
        LoopBreakingStrategy nestedLoopBreakingStrategy = EndlessLoopBreakingStrategy.nestingStrategy(nestingLevel);
        LoopBreakingStrategy defaultLoopBreakingStrategy = EndlessLoopBreakingStrategy.defaultStrategy()
                .getLoopBreakingStrategy();
        SelectionSetGenerator nestedBodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), nestedLoopBreakingStrategy);
        SelectionSetGenerator defaultBodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), defaultLoopBreakingStrategy);
        String nestedResult = nestedBodyGenerator.generate(new AllFieldsPickingStrategy());
        String defaultResult = defaultBodyGenerator.generate(new AllFieldsPickingStrategy());
        Assertions.assertEquals(nestedResult, defaultResult);
    }

    @Test
    void generateWithoutFieldsWithSelectionSetExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\") fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithoutFieldsWithSelectionSetExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField "
                + "customNamedField customNamedNonNullField nonNullField newFieldAtDescendant "
                + "newFieldWithoutAnnotationAtDescendant aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"test value\")}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void generateWithoutFieldsWithSelectionSetExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy(), new ExcludingLoopBreakingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField aliasForFieldWithAlias:fieldWithAlias "
                + "aliasForFieldWithAliasAndArguments:fieldWithAliasAndArguments("
                + "argumentForFieldWithAliasAndArguments1:1,argumentForFieldWithAliasAndArguments2:2) "
                + "fieldWithArgument(argumentForFieldWithArgument:\"valueForFieldWithArgument\") "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }
}
