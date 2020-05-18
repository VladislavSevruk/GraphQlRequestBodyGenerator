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

import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlInputArgument;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.WithoutNullsInputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.test.data.InheritedInputTestModel;
import com.github.vladislavsevruk.generator.test.data.SimpleSelectionSetTestModel;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GqlMutationBodyGeneratorTest {

    @Test
    void generateWithArgumentVarargsTest() {
        InheritedInputTestModel inputModel = new InheritedInputTestModel().setSubClassField("subClassFieldValue");
        inputModel.setTestField("testFieldValue");
        GqlInputArgument<InheritedInputTestModel> argument = GqlInputArgument.of(inputModel);
        SelectionSetGenerator selectionSetGenerator = new SelectionSetGenerator(
                new TypeMeta<>(SimpleSelectionSetTestModel.class), new AllExceptIgnoredFieldMarkingStrategy());
        String result = new GqlMutationBodyGenerator("customGqlMutation", selectionSetGenerator,
                new AllExceptIgnoredFieldMarkingStrategy())
                .generate(new WithoutNullsInputFieldsPickingStrategy(), new AllFieldsPickingStrategy(), argument);
        String expectedResult = "mutation{customGqlMutation(input:{subClassField:\"subClassFieldValue\","
                + "testField:\"testFieldValue\"}){selectionSetField}}";
        Assertions.assertEquals(expectedResult, result);
    }
}
