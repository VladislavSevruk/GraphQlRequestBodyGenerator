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
import com.github.vladislavsevruk.generator.strategy.input.type.InputTypePickingStrategyManager;
import com.github.vladislavsevruk.generator.test.data.InheritedInputTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import com.github.vladislavsevruk.generator.test.data.TestDataForInputTypeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GqlRequestBodyGeneratorTest {

    @Test
    void mutationTest() {
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        String result = GqlRequestBodyGenerator.mutation("customGqlMutation")
                .arguments(GqlInputArgument.of(inputModel))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"mutation{customGqlMutation(input:{subClassField:"
                + "\\\"subClassFieldValue\\\",testField:\\\"testFieldValue\\\"}){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void mutationTestWithVariables() {
        TestDataForInputTypeStrategy testData = new TestDataForInputTypeStrategy()
                .setName("user1")
                .setAddress("street1");
        String result = GqlRequestBodyGenerator.mutation("customGqlMutation")
                .arguments(InputTypePickingStrategyManager.withInputType(), GqlArgument.of("userProfile", testData))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"variables\":{\"userProfile\":{\"name\":\"user1\",\"address\":\"street1\"}}," +
                "\"query\":\"mutation($userProfile:InputData!){customGqlMutation(userProfile:$userProfile)" +
                "{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void queryTest() {
        String result = GqlRequestBodyGenerator.query("customGqlQuery")
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"query\":\"{customGqlQuery{selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void queryTestWithVariables() {
        TestDataForInputTypeStrategy testDataForQueryVariables = new TestDataForInputTypeStrategy()
                .setName("NameData")
                .setAddress("AddressData");
        String result = GqlRequestBodyGenerator.query("gqlQueryWithVariables")
                .arguments(InputTypePickingStrategyManager.withInputType(),
                        GqlArgument.of("search", testDataForQueryVariables))
                .selectionSet(SimpleSelectionSetTestModel.class).generate();
        String expectedResult = "{\"variables\":{\"name\":\"NameData\",\"address\":\"AddressData\"}," +
                "\"query\":\"query($search:InputData!){gqlQueryWithVariables(search:$search){selectionSetField}}\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
