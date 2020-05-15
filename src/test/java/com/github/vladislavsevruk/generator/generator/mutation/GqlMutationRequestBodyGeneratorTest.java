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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GqlMutationRequestBodyGeneratorTest {

    private InputFieldsPickingStrategy customInputFieldsPickingStrategy = (name, value) -> name.startsWith("custom")
            || value.contains("Ignored");

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
    }

    @Test
    public void argumentsArrayTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(GqlArgument.of("testArgument1", "testValue1"), GqlArgument.of("testArgument2", "testValue2"))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(testArgument1:\\\"testValue1\\\",testArgument2:"
                + "\\\"testValue2\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void argumentsListTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        List<GqlParameterValue<?>> arguments = Arrays
                .asList(GqlArgument.of("testArgument1", "testValue1"), GqlArgument.of("testArgument2", "testValue2"));
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation").arguments(arguments)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(testArgument1:\\\"testValue1\\\",testArgument2:"
                + "\\\"testValue2\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithDelegateAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod",
                "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                        + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"}");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithFieldAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithFieldAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithIgnoreAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod",
                "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithOverriddenNameExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("getFieldWithOverriddenNameWithDelegateMethod",
                "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                        + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + "fieldWithoutMethod:null," + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateAllInputFieldsWithoutAnnotationsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithDelegateMethod",
                "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateInputFieldsCustomStrategyExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation()
                .initFieldsWithIgnoreAnnotation().initFieldsWithoutAnnotations().initFieldsWithOverriddenName()
                .initFieldsWithDelegateAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
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
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod",
                "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                        + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"}");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:"
                + "\\\"fieldWithDelegateAnnotationWithoutMethod\\\"," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithFieldAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithFieldAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithIgnoreAnnotationExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithIgnoreAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod",
                "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{" + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithOverriddenNameExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithOverriddenNameWithDelegateMethod",
                "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                        + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\",customFieldWithOverriddenNameWithMethod:"
                + "\\\"getFieldWithOverriddenNameWithMethod method\\\",customFieldWithOverriddenNameWithoutMethod:{"
                + "nestedValue:\\\"fieldWithOverriddenNameWithoutMethod\\\"}," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateNonNullInputFieldsWithoutAnnotationsExceptIgnoredTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithDelegateMethod",
                "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithDelegateMethod:{"
                + "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"},"
                + "fieldWithDelegatePrivateMethod:{fieldFromFieldWithDelegatePrivateMethod:"
                + "\\\"value from fieldWithDelegatePrivateMethod\\\"},fieldWithIgnoredMethod:1,"
                + "fieldWithIgnoredPrivateMethod:[2,3],fieldWithInputMethod:\\\"fieldWithInputMethod method\\\","
                + "fieldWithInputPrivateMethod:\\\"fieldWithInputPrivateMethod\\\",fieldWithMethod:"
                + "\\\"fieldWithMethod method\\\",fieldWithPrivateMethod:\\\"fieldWithPrivateMethod\\\","
                + "fieldWithoutMethod:{nestedValue:\\\"fieldWithoutMethod\\\"}," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsCustomStrategyTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation()
                .initFieldsWithIgnoreAnnotation().initFieldsWithoutAnnotations().initFieldsWithOverriddenName()
                .initFieldsWithDelegateAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
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
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:null");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod",
                "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                        + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"}");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + "customFieldWithOverriddenNameWithoutMethod:null," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithFieldAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:null");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithFieldAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + "customFieldWithOverriddenNameWithoutMethod:null," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithIgnoreAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:null");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod",
                "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldWithFieldAnnotationWithDelegateMethod:null,fieldWithFieldAnnotationWithIgnoredMethod:null,"
                + "fieldWithFieldAnnotationWithInputMethod:null,fieldWithFieldAnnotationWithMethod:null,"
                + "fieldWithFieldAnnotationWithoutMethod:null,customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithOverriddenNameTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:null");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("getFieldWithOverriddenNameWithDelegateMethod",
                "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                        + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
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
                + "nestedValue:\\\"fieldWithOverriddenNameWithoutMethod\\\"}," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedInputFieldsWithoutAnnotationsTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.allFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithDelegateMethod",
                "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"");
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:\\\"fieldWithInputMethod method\\\"");
        methods.put("getFieldWithDelegateAnnotationWithInputMethod", "fieldWithDelegateAnnotationWithInputMethod:null");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod", "fieldWithIgnoreAnnotationWithInputMethod:null");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldWithFieldAnnotationWithDelegateMethod:null,fieldWithFieldAnnotationWithIgnoredMethod:null,"
                + "fieldWithFieldAnnotationWithInputMethod:null,fieldWithFieldAnnotationWithMethod:null,"
                + "fieldWithFieldAnnotationWithoutMethod:null,customFieldWithOverriddenNameWithDelegateMethod:null,"
                + "customFieldWithOverriddenNameWithIgnoredMethod:null,"
                + "customFieldWithOverriddenNameWithInputMethod:null,customFieldWithOverriddenNameWithMethod:null,"
                + "customFieldWithOverriddenNameWithoutMethod:null," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithDelegateAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithDelegateAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithDelegateAnnotationWithInputMethod",
                "fieldWithDelegateAnnotationWithInputMethod:{fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                        + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\"}");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "fieldFromGetFieldWithDelegateAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithDelegateMethod method\\\","
                + "fieldFromFieldWithDelegateAnnotationWithIgnoredMethod:"
                + "\\\"value from fieldWithDelegateAnnotationWithIgnoredMethod\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithInputMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithInputMethod method\\\","
                + "fieldFromGetFieldWithDelegateAnnotationWithMethod:"
                + "\\\"value from getFieldWithDelegateAnnotationWithMethod method\\\",nestedValue:\\\""
                + "fieldWithDelegateAnnotationWithoutMethod\\\"," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithFieldAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithFieldAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithFieldAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{fieldWithFieldAnnotationWithDelegateMethod:{"
                + "fieldFromGetFieldWithFieldAnnotationWithDelegateMethod:"
                + "\\\"value from getFieldWithFieldAnnotationWithDelegateMethod method\\\"},"
                + "fieldWithFieldAnnotationWithIgnoredMethod:\\\"fieldWithFieldAnnotationWithIgnoredMethod\\\","
                + "fieldWithFieldAnnotationWithInputMethod:\\\"getFieldWithFieldAnnotationWithInputMethod method\\\","
                + "fieldWithFieldAnnotationWithMethod:\\\"getFieldWithFieldAnnotationWithMethod method\\\","
                + "fieldWithFieldAnnotationWithoutMethod:{nestedValue:\\\"fieldWithFieldAnnotationWithoutMethod\\\"},"
                + getMethodValues(methods) + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithIgnoreAnnotationTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithIgnoreAnnotation();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithIgnoreAnnotationWithDelegateMethod",
                "fieldFromGetFieldWithIgnoreAnnotationWithDelegateMethod:"
                        + "\\\"value from getFieldWithIgnoreAnnotationWithDelegateMethod method\\\"");
        methods.put("getFieldWithIgnoreAnnotationWithInputMethod",
                "fieldWithIgnoreAnnotationWithInputMethod:\\\"getFieldWithIgnoreAnnotationWithInputMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{" + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithOverriddenNameTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithOverriddenName();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("getFieldWithOverriddenNameWithDelegateMethod",
                "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                        + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{"
                + "customFieldWithOverriddenNameWithDelegateMethod:{"
                + "fieldFromGetFieldWithOverriddenNameWithDelegateMethod:"
                + "\\\"value from getFieldWithOverriddenNameWithDelegateMethod method\\\"},"
                + "customFieldWithOverriddenNameWithIgnoredMethod:\\\"fieldWithOverriddenNameWithIgnoredMethod\\\","
                + "customFieldWithOverriddenNameWithInputMethod:"
                + "\\\"getFieldWithOverriddenNameWithInputMethod method\\\","
                + "customFieldWithOverriddenNameWithMethod:\\\"getFieldWithOverriddenNameWithMethod method\\\","
                + "customFieldWithOverriddenNameWithoutMethod:{nestedValue:"
                + "\\\"fieldWithOverriddenNameWithoutMethod\\\"}," + getMethodValues(methods)
                + "}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateOnlyMarkedNonNullInputFieldsWithoutAnnotationsTest() {
        FieldMarkingStrategySourceManager.input().useOnlyMarkedFieldsStrategy();
        TestInputModel inputModel = new TestInputModel().initFieldsWithoutAnnotations();
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        Map<String, String> methods = new HashMap<>();
        methods.put("fieldWithInputMethod", "fieldWithInputMethod:\\\"fieldWithInputMethod method\\\"");
        methods.put("fieldWithDelegateMethod",
                "fieldFromFieldWithDelegateMethod:\\\"value from fieldWithDelegateMethod method\\\"");
        methods.put("methodWithoutFieldWithDelegateAnnotation",
                "nestedValue:\\\"methodWithoutFieldWithDelegateAnnotation\\\"");
        methods.put("methodWithoutFieldWithInputAnnotation",
                "methodWithoutFieldWithInputAnnotation:{nestedValue:\\\"methodWithoutFieldWithInputAnnotation\\\"}");
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{" + getMethodValues(methods) + "})"
                + "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void generateWithoutSelectionSetTest() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new GqlMutationRequestBodyGenerator("customGqlMutation").generate());
    }

    @Test
    public void severalArgumentsTest() {
        FieldMarkingStrategySourceManager.input().useAllExceptIgnoredFieldsStrategy();
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        String result = new GqlMutationRequestBodyGenerator("customGqlMutation")
                .arguments(GqlInputArgument.of(inputModel), GqlArgument.of("testArgument", "testValue"))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"mutation\":\"{customGqlMutation(input:{subClassField:\\\"subClassFieldValue\\\","
                + "testField:\\\"testFieldValue\\\"},testArgument:\\\"testValue\\\"){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    // method to guarantee methods order match
    private String getMethodValues(Map<String, String> methodNameValues) {
        List<String> values = new ArrayList<>(methodNameValues.size());
        for (Method method : TestInputModel.class.getMethods()) {
            String methodName = method.getName();
            if (methodNameValues.containsKey(methodName)) {
                values.add(methodNameValues.get(methodName));
            }
        }
        return String.join(",", values);
    }
}
