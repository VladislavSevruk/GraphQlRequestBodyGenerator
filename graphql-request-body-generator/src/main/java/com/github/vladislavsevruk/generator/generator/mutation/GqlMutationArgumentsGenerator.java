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
package com.github.vladislavsevruk.generator.generator.mutation;

import com.github.vladislavsevruk.generator.generator.BaseGqlArgumentsGenerator;
import com.github.vladislavsevruk.generator.param.GqlDelegateArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates GraphQL mutation arguments according to different field picking strategies.
 */
@Log4j2
public class GqlMutationArgumentsGenerator extends BaseGqlArgumentsGenerator {

    public GqlMutationArgumentsGenerator(FieldMarkingStrategy inputFieldMarkingStrategy) {
        super(inputFieldMarkingStrategy);
    }

    /**
     * Generates GraphQL mutation arguments according to received field picking strategies.
     *
     * @param inputFieldsPickingStrategy        <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                          mutation input.
     * @param modelArgumentStrategy             <code>ModelArgumentStrategy</code> for mutation argument generation.
     * @param variablePickingStrategy           <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param arguments                         <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                          names and values.
     * @return <code>String</code> with resulted GraphQL mutation arguments.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        if (!arguments.iterator().hasNext()) {
            log.warn("GraphQL mutation argument iterable is empty.");
            return "";
        }
        return "(" + StreamSupport.stream(arguments.spliterator(), false)
                .map(argument -> generateArgumentValue(inputFieldsPickingStrategy, modelArgumentStrategy,
                        variablePickingStrategy, argument)).collect(Collectors.joining(",")) + ")";
    }

    private String generateArgumentValue(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, VariablePickingStrategy variablePickingStrategy,
            GqlParameterValue<?> argument) {
        if (variablePickingStrategy.isVariable(argument)) {
            return argument.getName() + ":$" + variablePickingStrategy.getVariableName(argument);
        }
        if (isDelegate(argument)) {
            boolean withVariables = ((GqlDelegateArgument<?>) argument).isShouldUseVariables();
            return generateModelArguments(argument.getValue(), inputFieldsPickingStrategy, withVariables);
        }
        if (!modelArgumentStrategy.isModelArgument(argument)) {
            return argument.getName() + ":" + StringUtil.generateEscapedValueString(argument.getValue());
        }
        Object inputValue = argument.getValue();
        log.debug("Generating '{}' argument value for '{}' model using '{}' input field "
                        + "marking strategy and '{}' field picking strategy.", argument.getName(),
                inputValue.getClass().getName(), inputFieldMarkingStrategy.getClass().getName(),
                inputFieldsPickingStrategy.getClass().getName());
        return argument.getName() + ":" + generateArgumentModelValue(inputValue, inputFieldsPickingStrategy, true);
    }
}
