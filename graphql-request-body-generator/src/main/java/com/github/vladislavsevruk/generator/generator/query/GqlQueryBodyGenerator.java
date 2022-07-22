/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Uladzislau Seuruk
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

import com.github.vladislavsevruk.generator.generator.GqlBodyGenerator;
import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates body for GraphQL queries with received arguments and selection set according to different field picking
 * strategies.
 */
@Log4j2
public class GqlQueryBodyGenerator extends GqlBodyGenerator {

    private final String queryName;
    private final SelectionSetGenerator selectionSetGenerator;

    public GqlQueryBodyGenerator(String queryName, SelectionSetGenerator selectionSetGenerator) {
        Objects.requireNonNull(selectionSetGenerator);
        this.selectionSetGenerator = selectionSetGenerator;
        this.queryName = queryName;
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
        log.info(() -> String.format("Generating '%s' GraphQL query.", queryName));
        String selectionSet = selectionSetGenerator.generate(fieldsPickingStrategy);
        String variablesStr = generateVariables(variablePickingStrategy, arguments);
        String operationArgumentsStr = generateOperationArguments(variablePickingStrategy, arguments);
        String query = "{" + queryName + generateGqlArguments(variablePickingStrategy, arguments) + selectionSet + "}";
        if (StringUtil.isNotBlank(operationAlias) || !variablesStr.isEmpty()) {
            String operationPrefix = StringUtil.isNotBlank(operationAlias) ? "query " + operationAlias : "query";
            query = operationPrefix + operationArgumentsStr + query;
        }
        String wrappedQuery = wrapForRequestBody(query, variablesStr);
        log.debug(() -> "Resulted query: " + wrappedQuery);
        return wrappedQuery;
    }

    private String generateArgumentValue(VariablePickingStrategy variablePickingStrategy,
            GqlParameterValue<?> argument) {
        if (variablePickingStrategy.isVariable(argument)) {
            return argument.getName() + ":$" + variablePickingStrategy.getVariableName(argument);
        }
        return argument.getName() + ":" + StringUtil.generateEscapedValueString(argument.getValue());
    }

    private String generateGqlArguments(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> argumentValue) {
        if (!argumentValue.iterator().hasNext()) {
            log.debug("GraphQL query argument iterable is empty.");
            return "";
        }
        return "(" + StreamSupport.stream(argumentValue.spliterator(), false)
                .map(argument -> generateArgumentValue(variablePickingStrategy, argument))
                .collect(Collectors.joining(",")) + ")";
    }
}
