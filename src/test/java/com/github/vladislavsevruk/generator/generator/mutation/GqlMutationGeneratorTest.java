/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2020 Uladzislau Seuruk
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */
package com.github.vladislavsevruk.generator.generator.mutation;

import com.github.vladislavsevruk.generator.param.GqlArgument;
import com.github.vladislavsevruk.generator.param.GqlInputArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.test.data.InheritedInputTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import com.github.vladislavsevruk.generator.test.data.TestInputModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class GqlMutationGeneratorTest {

    private InputFieldsPickingStrategy customInputFieldsPickingStrategy = (name, value) -> name.startsWith("custom")
            || value.contains("Ignored");

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
    }

    @Test
    public void argumentsArrayTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        List<GqlParameterValue<?>> arguments = Arrays
                .asList(GqlArgument.of("testArgument1", "testValue1"), GqlArgument.of("testArgument2", "testValue2"));
        String result = new GqlMutationGenerator("customGqlMutation").arguments(arguments)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(testArgument1:\\\"testValue1\\\",testArgument2:"
                + "\\\"testValue2\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithDelegateAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:"
                + "\\\"fieldWithDelegateAnnotationWithoutMethod\\\",fieldWithDelegateMethod:null,"
                + "fieldWithDelegatePrivateMethod:null,fieldWithFieldAnnotationWithDelegateMethod:null,"
                + "fieldWithFieldAnnotationWithIgnoredMethod:null,fieldWithFieldAnnotationWithInputMethod:null,"
                + "fieldWithFieldAnnotationWithMethod:null,fieldWithFieldAnnotationWithoutMethod:null,"
                + "fieldWithIgnoredMethod:null,fieldWithIgnoredPrivateMethod:null,fieldWithInputMethod:null,"
                + "fieldWithInputPrivateMethod:null,fieldWithMethod:null,"
                + "customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithPrivateMethod:null,fieldWithoutMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"},"
                + "fieldWithIgnoreAnnotationWithInputMethod:null,methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithFieldAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:null,"
                + "fieldWithDelegatePrivateMethod:null,fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + "fieldWithIgnoredMethod:null,fieldWithIgnoredPrivateMethod:null,fieldWithInputMethod:null,"
                + "fieldWithInputPrivateMethod:null,fieldWithMethod:null,"
                + "customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithPrivateMethod:null,fieldWithoutMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:null,nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithIgnoreAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:null,"
                + "fieldWithDelegatePrivateMethod:null,fieldWithFieldAnnotationWithDelegateMethod:null,"
                + "fieldWithFieldAnnotationWithIgnoredMethod:null,fieldWithFieldAnnotationWithInputMethod:null,"
                + "fieldWithFieldAnnotationWithMethod:null,fieldWithFieldAnnotationWithoutMethod:null,"
                + "fieldWithIgnoredMethod:null,fieldWithIgnoredPrivateMethod:null,fieldWithInputMethod:null,"
                + "fieldWithInputPrivateMethod:null,fieldWithMethod:null,"
                + "customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithPrivateMethod:null,fieldWithoutMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\","
                + "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithOverriddenNameExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:null,"
                + "fieldWithDelegatePrivateMethod:null,fieldWithFieldAnnotationWithDelegateMethod:null,"
                + "fieldWithFieldAnnotationWithIgnoredMethod:null,fieldWithFieldAnnotationWithInputMethod:null,"
                + "fieldWithFieldAnnotationWithMethod:null,fieldWithFieldAnnotationWithoutMethod:null,"
                + "fieldWithIgnoredMethod:null,fieldWithIgnoredPrivateMethod:null,fieldWithInputMethod:null,"
                + "fieldWithInputPrivateMethod:null,fieldWithMethod:null,"
                + "customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{"
                + "nestedValue:\\\"fieldWithOverriddenNameWithoutMethod\\\"},fieldWithPrivateMethod:null,"
                + "fieldWithoutMethod:null,fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldWithIgnoreAnnotationWithInputMethod:null,fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithoutAnnotationsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:{"
                + "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"},"
                + "fieldWithDelegatePrivateMethod:{fieldFromFieldWithDelegatePrivateMethod:"
                + "\\\"value from fieldWithDelegatePrivateMethod\\\"},fieldWithFieldAnnotationWithDelegateMethod:null,"
                + "fieldWithFieldAnnotationWithIgnoredMethod:null,fieldWithFieldAnnotationWithInputMethod:null,"
                + "fieldWithFieldAnnotationWithMethod:null,fieldWithFieldAnnotationWithoutMethod:null,"
                + "fieldWithIgnoredMethod:1,fieldWithIgnoredPrivateMethod:[2,3],fieldWithInputMethod:"
                + "\\\"fieldWithInputMethod method\\\",fieldWithInputPrivateMethod:\\\"fieldWithInputPrivateMethod\\\","
                + "fieldWithMethod:\\\"fieldWithMethod method\\\",customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithPrivateMethod:"
                + "\\\"fieldWithPrivateMethod\\\",fieldWithoutMethod:{nestedValue:\\\"fieldWithoutMethod\\\"},"
                + "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\","
                + "fieldWithDelegateAnnotationWithInputMethod:null,fieldWithIgnoreAnnotationWithInputMethod:null,"
                + "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateInputFieldsCustomStrategyExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation()
                .initFieldsWithIgnoreAnnotation().initFieldsWithoutAnnotations().initFieldsWithOverriddenName()
                .initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(customInputFieldsPickingStrategy, GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithDelegateMethod:{},customFieldWithOverriddenNameWithIgnoredMethod:"
                + "\\\"fieldWithOverriddenNameWithIgnoredMethod\\\",customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{}})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithDelegateAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:"
                + "\\\"fieldWithDelegateAnnotationWithoutMethod\\\","
                + "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"},"
                + "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithFieldAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithIgnoreAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\","
                + "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithOverriddenNameExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{"
                + "nestedValue:\\\"fieldWithOverriddenNameWithoutMethod\\\"},"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{nestedValue:"
                + "\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithoutAnnotationsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:{"
                + "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"},"
                + "fieldWithDelegatePrivateMethod:{fieldFromFieldWithDelegatePrivateMethod:"
                + "\\\"value from fieldWithDelegatePrivateMethod\\\"},fieldWithIgnoredMethod:1,"
                + "fieldWithIgnoredPrivateMethod:[2,3],fieldWithInputMethod:\\\"fieldWithInputMethod method\\\","
                + "fieldWithInputPrivateMethod:\\\"fieldWithInputPrivateMethod\\\",fieldWithMethod:"
                + "\\\"fieldWithMethod method\\\",fieldWithPrivateMethod:\\\"fieldWithPrivateMethod\\\","
                + "fieldWithoutMethod:{nestedValue:\\\"fieldWithoutMethod\\\"},fieldFromFieldWithDelegateMethod:"
                + "\\\"value from fieldWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsCustomStrategyTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation()
                .initFieldsWithIgnoreAnnotation().initFieldsWithoutAnnotations().initFieldsWithOverriddenName()
                .initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(customInputFieldsPickingStrategy, GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithDelegateMethod:{},customFieldWithOverriddenNameWithIgnoredMethod:"
                + "\\\"fieldWithOverriddenNameWithIgnoredMethod\\\",customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{}})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithDelegateAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:"
                + "\\\"fieldWithDelegateAnnotationWithoutMethod\\\",fieldWithFieldAnnotationWithDelegateMethod:null,"
                + "fieldWithFieldAnnotationWithIgnoredMethod:null,fieldWithFieldAnnotationWithInputMethod:null,"
                + "fieldWithFieldAnnotationWithMethod:null,fieldWithFieldAnnotationWithoutMethod:null,"
                + "customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithInputMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"},"
                + "fieldWithIgnoreAnnotationWithInputMethod:null,methodWithoutFieldWithInputAnnotation:{nestedValue:"
                + "\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithFieldAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + "customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithInputMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:null,nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithIgnoreAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldWithFieldAnnotationWithDelegateMethod:null,fieldWithFieldAnnotationWithIgnoredMethod:null,"
                + "fieldWithFieldAnnotationWithInputMethod:null,fieldWithFieldAnnotationWithMethod:null,"
                + "fieldWithFieldAnnotationWithoutMethod:null,customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldWithInputMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\","
                + "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithOverriddenNameTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldWithFieldAnnotationWithDelegateMethod:null,fieldWithFieldAnnotationWithIgnoredMethod:null,"
                + "fieldWithFieldAnnotationWithInputMethod:null,fieldWithFieldAnnotationWithMethod:null,"
                + "fieldWithFieldAnnotationWithoutMethod:null,customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{"
                + "nestedValue:\\\"fieldWithOverriddenNameWithoutMethod\\\"},fieldWithInputMethod:null,"
                + "fieldWithDelegateAnnotationWithInputMethod:null,fieldWithIgnoreAnnotationWithInputMethod:null,"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{nestedValue:"
                + "\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithoutAnnotationsTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldWithFieldAnnotationWithDelegateMethod:null,fieldWithFieldAnnotationWithIgnoredMethod:null,"
                + "fieldWithFieldAnnotationWithInputMethod:null,fieldWithFieldAnnotationWithMethod:null,"
                + "fieldWithFieldAnnotationWithoutMethod:null,customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null,fieldFromFieldWithDelegateMethod:"
                + "\\\"value from fieldWithDelegateMethod method\\\",fieldWithInputMethod:"
                + "\\\"fieldWithInputMethod method\\\",fieldWithDelegateAnnotationWithInputMethod:null,"
                + "fieldWithIgnoreAnnotationWithInputMethod:null,nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithDelegateAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:\\\""
                + "fieldWithDelegateAnnotationWithoutMethod\\\",fieldWithDelegateAnnotationWithInputMethod:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"},"
                + "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithFieldAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{nestedValue:"
                + "\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithIgnoreAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\","
                + "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\","
                + "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{"
                + "nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithOverriddenNameTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\","
                + "customFieldWithOverriddenNameWithMethod:\\\"getFieldWithOverriddenNameWithMethod method\\\","
                + "customFieldWithOverriddenNameWithoutMethod:{nestedValue:"
                + "\\\"fieldWithOverriddenNameWithoutMethod\\\"},fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\",nestedValue:"
                + "\\\"methodWithoutFieldWithDelegateAnnotation\\\",methodWithoutFieldWithInputAnnotation:{nestedValue:"
                + "\\\"methodWithoutFieldWithInputAnnotation\\\"}}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithoutAnnotationsTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldFromFieldWithDelegateMethod:"
                + "\\\"value from fieldWithDelegateMethod method\\\",fieldWithInputMethod:"
                + "\\\"fieldWithInputMethod method\\\",nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\","
                + "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutSelectionSetTest() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new GqlMutationGenerator("customGqlMutation").generate());
    }

    @Test
    public void severalArgumentsTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        String result = new GqlMutationGenerator("customGqlMutation")
                .arguments(GqlInputArgument.of(inputModel), GqlArgument.of("testArgument", "testValue"))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{subClassField:\\\"subClassFieldValue\\\","
                + "testField:\\\"testFieldValue\\\"},testArgument:\\\"testValue\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
