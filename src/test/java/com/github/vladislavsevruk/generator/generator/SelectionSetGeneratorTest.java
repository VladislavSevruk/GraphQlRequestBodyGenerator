package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.OnlyMarkedFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyIdFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyNonNullableFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.WithoutFieldsWithSelectionSetPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.GenericTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.InheritedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.InheritedTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.NestedTestModelWithoutAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithAnnotations;
import com.github.vladislavsevruk.generator.test.data.TestModelWithoutAnnotations;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelectionSetGeneratorTest {

    @Test
    public void generateAllExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id customNamedField customNamedNonNullableField nonNullableField} collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} listEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField} customNamedNonNullableEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} nonNullableEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullableField nonNullableField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField nonNullableField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity collectionField fieldWithDelegateAnnotation "
                + "fieldWithEntityAnnotation fieldWithFieldAnnotation fieldWithIgnoreAnnotation "
                + "fieldWithoutAnnotations id idField listEntity namedEntity namedField namedNonNullableEntity "
                + "namedNonNullableField nonNullableEntity nonNullableField queueEntity setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation "
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
                + "customNamedField customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id newDelegateAtDescendant newEntityAtDescendant "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant newIgnoredFieldAtDescendant "
                + "collectionEntity collectionField fieldWithDelegateAnnotation fieldWithEntityAnnotation "
                + "fieldWithFieldAnnotation fieldWithIgnoreAnnotation fieldWithoutAnnotations idField listEntity "
                + "namedEntity namedField namedNonNullableEntity namedNonNullableField nonNullableEntity "
                + "nonNullableField queueEntity setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "customNamedField customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullableExceptIgnoredModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} "
                + "nonNullableEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullableEntity{id} nonNullableEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField "
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
                + "idField id customNamedField customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullableField nonNullableField "
                + "customNamedNonNullableEntity{customNamedNonNullableField nonNullableField} nonNullableEntity{"
                + "customNamedNonNullableField nonNullableField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullableModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullableFieldsPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField "
                + "customNamedField customNamedNonNullableField nonNullableField newFieldAtDescendant}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModelWithAnnotations<NestedTestModelWithAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullableField "
                + "nonNullableField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredGenericModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta
                = new TypeProvider<GenericTestModelWithoutAnnotations<NestedTestModelWithoutAnnotations>>() {}
                .getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionEntity collectionField "
                + "fieldWithDelegateAnnotation fieldWithEntityAnnotation fieldWithFieldAnnotation "
                + "fieldWithIgnoreAnnotation fieldWithoutAnnotations id idField listEntity namedEntity namedField "
                + "namedNonNullableEntity namedNonNullableField nonNullableEntity nonNullableField queueEntity "
                + "setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField customNamedField customNamedNonNullableField nonNullableField "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredInheritedModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id newDelegateAtDescendant newEntityAtDescendant "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant newIgnoredFieldAtDescendant "
                + "collectionEntity collectionField fieldWithDelegateAnnotation fieldWithEntityAnnotation "
                + "fieldWithFieldAnnotation fieldWithIgnoreAnnotation fieldWithoutAnnotations idField listEntity "
                + "namedEntity namedField namedNonNullableEntity namedNonNullableField nonNullableEntity "
                + "nonNullableField queueEntity setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullableField nonNullableField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredModelWithoutAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModelWithoutAnnotations.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionEntity collectionField fieldWithDelegateAnnotation "
                + "fieldWithEntityAnnotation fieldWithFieldAnnotation fieldWithIgnoreAnnotation "
                + "fieldWithoutAnnotations id idField listEntity namedEntity namedField namedNonNullableEntity "
                + "namedNonNullableField nonNullableEntity nonNullableField queueEntity setEntity}";
        Assertions.assertEquals(expectedResult, result);
    }
}
