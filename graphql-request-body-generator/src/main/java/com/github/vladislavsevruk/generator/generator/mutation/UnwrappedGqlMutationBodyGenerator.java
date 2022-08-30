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

import com.github.vladislavsevruk.generator.generator.GqlVariableArgumentsGenerator;
import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Objects;

/**
 * Generates unwrapped GraphQL mutations with received arguments and selection set according to different field picking
 * strategies.
 */
@Log4j2
public class UnwrappedGqlMutationBodyGenerator {

    private final String mutationName;
    private final GqlMutationArgumentsGenerator mutationArgumentsGenerator;
    private final GqlVariableArgumentsGenerator operationArgumentsGenerator;
    private final SelectionSetGenerator selectionSetGenerator;

    public UnwrappedGqlMutationBodyGenerator(String mutationName, SelectionSetGenerator selectionSetGenerator) {
        this(mutationName, selectionSetGenerator, FieldMarkingStrategySourceManager.input().getStrategy());
    }

    public UnwrappedGqlMutationBodyGenerator(String mutationName, SelectionSetGenerator selectionSetGenerator,
            FieldMarkingStrategy inputFieldMarkingStrategy) {
        Objects.requireNonNull(selectionSetGenerator);
        this.selectionSetGenerator = selectionSetGenerator;
        this.mutationName = mutationName;
        this.mutationArgumentsGenerator = new GqlMutationArgumentsGenerator(inputFieldMarkingStrategy);
        this.operationArgumentsGenerator = new GqlVariableArgumentsGenerator(inputFieldMarkingStrategy);
    }

    /**
     * Generates GraphQL mutation body with received arguments according to received field picking strategies.
     *
     * @param inputFieldsPickingStrategy        <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                          mutation input.
     * @param modelArgumentStrategy             <code>ModelArgumentStrategy</code> for mutation argument generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for
     *                                          mutation selection set.
     * @param variablePickingStrategy           <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param operationAlias                    <code>String</code> with alias that should be used for GraphQL operation
     *                                          generation.
     * @param arguments                         <code>GqlParameterValue</code> varargs with argument names and values.
     * @return <code>String</code> with resulted GraphQL mutation.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, String operationAlias, GqlParameterValue<?>... arguments) {
        return generate(inputFieldsPickingStrategy, modelArgumentStrategy, selectionSetFieldsPickingStrategy,
                variablePickingStrategy, operationAlias, Arrays.asList(arguments));
    }

    /**
     * Generates GraphQL mutation body with received arguments according to received field picking strategies.
     *
     * @param inputFieldsPickingStrategy        <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                          mutation input.
     * @param modelArgumentStrategy             <code>ModelArgumentStrategy</code> for mutation argument generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for
     *                                          mutation selection set.
     * @param variablePickingStrategy           <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param operationAlias                    <code>String</code> with alias that should be used for GraphQL operation
     *                                          generation.
     * @param arguments                         <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                          names and values.
     * @return <code>String</code> with resulted GraphQL mutation.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, String operationAlias,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        Objects.requireNonNull(arguments);
        log.info("Generating '{}' GraphQL mutation.", mutationName);
        String selectionSet = selectionSetGenerator.generate(selectionSetFieldsPickingStrategy);
        String operationArgumentsStr = operationArgumentsGenerator.generate(inputFieldsPickingStrategy,
                variablePickingStrategy, arguments);
        String argumentsStr = mutationArgumentsGenerator.generate(inputFieldsPickingStrategy, modelArgumentStrategy,
                variablePickingStrategy, arguments);
        String operationPrefix = StringUtil.isNotBlank(operationAlias) ? "mutation " + operationAlias : "mutation";
        String mutation = operationPrefix + operationArgumentsStr + "{" + mutationName + argumentsStr + selectionSet
                + "}";
        log.debug("Resulted mutation: {}", mutation);
        return mutation;
    }
}
