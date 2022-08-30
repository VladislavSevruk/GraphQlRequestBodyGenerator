/*
 * MIT License
 *
 * Copyright (c) 2022 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.generator.query;

import com.github.vladislavsevruk.generator.generator.BaseGqlArgumentsGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates GraphQL query arguments according to different field picking strategies.
 */
@Log4j2
public class GqlQueryArgumentsGenerator extends BaseGqlArgumentsGenerator {

    public GqlQueryArgumentsGenerator() {
        super(new AllExceptIgnoredFieldMarkingStrategy());
    }

    /**
     * Builds GraphQL query arguments according to received field picking strategy.
     *
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param arguments               <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        if (!arguments.iterator().hasNext()) {
            log.debug("GraphQL query argument iterable is empty.");
            return "";
        }
        return "(" + StreamSupport.stream(arguments.spliterator(), false)
                .map(argument -> generateArgumentValue(variablePickingStrategy, argument))
                .collect(Collectors.joining(",")) + ")";
    }

    private String generateArgumentValue(VariablePickingStrategy variablePickingStrategy,
            GqlParameterValue<?> argument) {
        if (variablePickingStrategy.isVariable(argument)) {
            return argument.getName() + ":$" + variablePickingStrategy.getVariableName(argument);
        }
        if (isDelegate(argument)) {
            return generateModelArguments(argument.getValue(),
                    InputGenerationStrategy.nonNullsFields().getInputFieldsPickingStrategy());
        }
        return argument.getName() + ":" + StringUtil.generateEscapedValueString(argument.getValue());
    }
}
