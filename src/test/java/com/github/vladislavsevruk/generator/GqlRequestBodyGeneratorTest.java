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

import com.github.vladislavsevruk.generator.param.GqlArgument;
import com.github.vladislavsevruk.generator.param.GqlInputArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.param.GqlVariableArgument;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import com.github.vladislavsevruk.generator.test.data.AnnotatedVariableAllMethodsTestModel;
import com.github.vladislavsevruk.generator.test.data.AnnotatedVariableRequiredTestModel;
import com.github.vladislavsevruk.generator.test.data.InheritedInputTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class GqlRequestBodyGeneratorTest {

    @Test
    void mutationTest() {
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        String result = GqlRequestBodyGenerator.mutation("customGqlMutation").arguments(GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"mutation{customGqlMutation(input:{subClassField:"
                + "\\\"subClassFieldValue\\\",testField:\\\"testFieldValue\\\"}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void mutationWithSeveralVariableArgumentsTest() {
        AnnotatedVariableAllMethodsTestModel testData1 = new AnnotatedVariableAllMethodsTestModel().setName("user1")
                .setAddress("street1");
        AnnotatedVariableAllMethodsTestModel testData2 = new AnnotatedVariableAllMethodsTestModel().setName("user2")
                .setAddress("street2");
        String variableName1 = "userProfile1";
        String variableName2 = "userProfile2";
        GqlParameterValue<?> variable1 = GqlVariableArgument.of(variableName1, testData1, true);
        GqlParameterValue<?> variable2 = GqlVariableArgument
                .of(variableName2, variableName2, testData2, "InputData", false);
        List<GqlParameterValue<?>> variables = Arrays.asList(variable1, variable2);
        String result = GqlRequestBodyGenerator.mutation("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), VariableGenerationStrategy.byArgumentType(),
                        ModelArgumentGenerationStrategy.anyArgument(), variables)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"variables\":{\"userProfile1\":{\"address\":\"street1\",\"name\":\"user1\"},"
                + "\"userProfile2\":{\"address\":\"street2\",\"name\":\"user2\"}},"
                + "\"query\":\"mutation($userProfile1:AnnotatedVariableAllMethodsTestModel!,$userProfile2:InputData){"
                + "customGqlMutation(userProfile1:$userProfile1,userProfile2:$userProfile2){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void mutationWithVariableArgumentAndSimpleArgumentTest() {
        AnnotatedVariableAllMethodsTestModel testData1 = new AnnotatedVariableAllMethodsTestModel().setName("user1")
                .setAddress("street1");
        AnnotatedVariableAllMethodsTestModel testData2 = new AnnotatedVariableAllMethodsTestModel().setName("user2")
                .setAddress("street2");
        String variableName = "userProfile1";
        GqlParameterValue<?> variable = GqlVariableArgument.of(variableName, testData1);
        GqlParameterValue<?> argument = GqlArgument.of("userProfile2", testData2);
        String result = GqlRequestBodyGenerator.mutation("customGqlMutation")
                .arguments(InputGenerationStrategy.nonNullsFields(), VariableGenerationStrategy.byArgumentType(),
                        ModelArgumentGenerationStrategy.anyArgument(), variable, argument)
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"variables\":{\"userProfile1\":{\"address\":\"street1\",\"name\":\"user1\"}},"
                + "\"query\":\"mutation($userProfile1:AnnotatedVariableAllMethodsTestModel){customGqlMutation("
                + "userProfile1:$userProfile1,userProfile2:{address:\\\"street2\\\",name:\\\"user2\\\"}){"
                + "selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void queryTest() {
        String result = GqlRequestBodyGenerator.query("customGqlQuery").selectionSet(SimpleSelectionSetTestModel.class)
                .generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void queryWithAnnotatedAllMethodsVariableTest() {
        AnnotatedVariableAllMethodsTestModel testVariable = new AnnotatedVariableAllMethodsTestModel()
                .setName("NameData").setAddress("AddressData");
        String result = GqlRequestBodyGenerator.query("gqlQueryWithVariables")
                .arguments(VariableGenerationStrategy.annotatedArgumentValueType(),
                        GqlArgument.of("search", testVariable)).selectionSet(SimpleSelectionSetTestModel.class)
                .generate();
        String expectedResult = "{\"variables\":{\"globalSearch\":{\"address\":\"AddressData\",\"name\":\"NameData\"}},"
                + "\"query\":\"query($globalSearch:InputData={\\\"name\\\":\\\"Test Name\\\","
                + "\\\"address\\\":\\\"Test Address\\\"}){gqlQueryWithVariables(search:$globalSearch){"
                + "selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void queryWithAnnotatedRequiredVariableTest() {
        AnnotatedVariableRequiredTestModel testVariable = new AnnotatedVariableRequiredTestModel().setName("NameData")
                .setAddress("AddressData");
        String result = GqlRequestBodyGenerator.query("gqlQueryWithVariables")
                .arguments(VariableGenerationStrategy.annotatedArgumentValueType(),
                        GqlArgument.of("search", testVariable)).selectionSet(SimpleSelectionSetTestModel.class)
                .generate();
        String expectedResult = "{\"variables\":{\"search\":{\"address\":\"AddressData\",\"name\":\"NameData\"}},"
                + "\"query\":\"query($search:AnnotatedVariableRequiredTestModel!){"
                + "gqlQueryWithVariables(search:$search){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
