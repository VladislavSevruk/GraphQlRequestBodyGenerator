package com.github.vladislavsevruk.generator.generator;

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
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelectionSetGeneratorTest {

    @Test
    public void generateAllExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations "
                + "idField id customNamedField customNamedNonNullField nonNullField} collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField fieldWithEntityAnnotation{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} listEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} customNamedNonNullEntity{"
                + "collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation fieldWithoutAnnotations idField id customNamedField "
                + "customNamedNonNullField nonNullField} queueEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
                + "setEntity{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField customNamedField customNamedNonNullField nonNullField "
                + "newEntityAtDescendant{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField} newFieldAtDescendant "
                + "newFieldWithoutAnnotationAtDescendant collectionEntity{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField nonNullField} "
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
                + "customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyIdExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField " + "nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{id newEntityAtDescendant{id} collectionEntity{id} "
                + "fieldWithEntityAnnotation{id} listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} "
                + "nonNullEntity{id} queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedIdModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyIdFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{id} id fieldWithEntityAnnotation{id} "
                + "listEntity{id} customNamedEntity{id} customNamedNonNullEntity{id} nonNullEntity{id} "
                + "queueEntity{id} setEntity{id}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField "
                + "customNamedField customNamedNonNullField nonNullField newEntityAtDescendant{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "newFieldAtDescendant collectionEntity{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField} fieldWithEntityAnnotation{"
                + "collectionField fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField "
                + "nonNullField} listEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} customNamedEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "customNamedNonNullEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} nonNullEntity{collectionField "
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField nonNullField} "
                + "queueEntity{collectionField fieldWithFieldAnnotation idField id customNamedField "
                + "customNamedNonNullField nonNullField} setEntity{collectionField fieldWithFieldAnnotation "
                + "idField id customNamedField customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new AllFieldsPickingStrategy());
        String expectedResult = "{collectionEntity{collectionField "
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
                + "fieldWithFieldAnnotation idField id customNamedField customNamedNonNullField " + "nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new OnlyNonNullFieldsPickingStrategy());
        String expectedResult = "{customNamedNonNullField nonNullField "
                + "customNamedNonNullEntity{customNamedNonNullField nonNullField} nonNullEntity{"
                + "customNamedNonNullField nonNullField}}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation idField "
                + "customNamedField customNamedNonNullField nonNullField newFieldAtDescendant}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedWithoutFieldsWithSelectionSetModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new OnlyMarkedFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation idField id "
                + "customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredGenericModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeProvider<GenericTestModel<NestedTestModel>>() {}.getTypeMeta();
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField id customNamedField customNamedNonNullField " + "nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredInheritedModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(InheritedTestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{id collectionField fieldWithFieldAnnotation "
                + "fieldWithoutAnnotations idField customNamedField customNamedNonNullField nonNullField "
                + "newFieldAtDescendant newFieldWithoutAnnotationAtDescendant}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutFieldsWithSelectionSetExceptIgnoredModelWithAnnotationsTest() {
        TypeMeta<?> modelMeta = new TypeMeta<>(TestModel.class);
        SelectionSetGenerator bodyGenerator = new SelectionSetGenerator(modelMeta,
                new AllExceptIgnoredFieldMarkingStrategy());
        String result = bodyGenerator.generate(new WithoutFieldsWithSelectionSetPickingStrategy());
        String expectedResult = "{collectionField fieldWithFieldAnnotation fieldWithoutAnnotations idField id "
                + "customNamedField customNamedNonNullField nonNullField}";
        Assertions.assertEquals(expectedResult, result);
    }
}
