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

import com.github.vladislavsevruk.generator.generator.GqlVariableArgumentsGenerator;
import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Objects;

/**
 * Generates unwrapped GraphQL queries with received arguments and selection set according to different field picking
 * strategies.
 */
@Log4j2
public class UnwrappedGqlQueryBodyGenerator {

    private final String queryName;
    private final GqlQueryArgumentsGenerator queryArgumentsGenerator = new GqlQueryArgumentsGenerator();
    private final GqlVariableArgumentsGenerator operationArgumentsGenerator;
    private final SelectionSetGenerator selectionSetGenerator;

    public UnwrappedGqlQueryBodyGenerator(String queryName, SelectionSetGenerator selectionSetGenerator) {
        this(queryName, selectionSetGenerator, FieldMarkingStrategySourceManager.input().getStrategy());
    }

    public UnwrappedGqlQueryBodyGenerator(String queryName, SelectionSetGenerator selectionSetGenerator,
            FieldMarkingStrategy inputFieldMarkingStrategy) {
        Objects.requireNonNull(selectionSetGenerator);
        Objects.requireNonNull(inputFieldMarkingStrategy);
        this.selectionSetGenerator = selectionSetGenerator;
        this.queryName = queryName;
        this.operationArgumentsGenerator = new GqlVariableArgumentsGenerator(inputFieldMarkingStrategy);
    }

    /**
     * Builds GraphQL query body with received arguments according to received field picking strategy.
     *
     * @param fieldsPickingStrategy   <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param operationAlias          <code>String</code> with alias that should be used for GraphQL operation
     *                                generation.
     * @param arguments               <code>GqlArgument</code> varargs with argument names and values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy, VariablePickingStrategy variablePickingStrategy,
            String operationAlias, GqlParameterValue<?>... arguments) {
        return generate(fieldsPickingStrategy, variablePickingStrategy, operationAlias, Arrays.asList(arguments));
    }

    /**
     * Builds GraphQL query body with received arguments according to received field picking strategy.
     *
     * @param fieldsPickingStrategy   <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> for mutation variables generation.
     * @param operationAlias          <code>String</code> with alias that should be used for GraphQL operation
     *                                generation.
     * @param arguments               <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy, VariablePickingStrategy variablePickingStrategy,
            String operationAlias, Iterable<? extends GqlParameterValue<?>> arguments) {
        Objects.requireNonNull(arguments);
        log.info("Generating '{}' GraphQL query.", queryName);
        String selectionSet = selectionSetGenerator.generate(fieldsPickingStrategy);
        String operationArgumentsStr = operationArgumentsGenerator.generate(variablePickingStrategy, arguments);
        String query = "{" + queryName + queryArgumentsGenerator.generate(variablePickingStrategy, arguments)
                + selectionSet + "}";
        if (StringUtil.isNotBlank(operationAlias) || StringUtil.isNotBlank(operationArgumentsStr)) {
            String operationPrefix = StringUtil.isNotBlank(operationAlias) ? "query " + operationAlias : "query";
            query = operationPrefix + operationArgumentsStr + query;
        }
        log.debug("Resulted query: {}", query);
        return query;
    }
}
