/*
 * MIT License
 *
 * Copyright (c) 2021 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class body generator with common logic for GraphQL operation.
 */
@Log4j2
public class GqlBodyGenerator {

    protected static ObjectMapper jsonMapper = new ObjectMapper();

    protected String generateOperationArgument(VariablePickingStrategy variablePickingStrategy,
            GqlParameterValue<?> argument) {
        String variableName = variablePickingStrategy.getVariableName(argument);
        String variableType = variablePickingStrategy.getVariableType(argument);
        String requiredArgumentPostfix = Optional.ofNullable(variablePickingStrategy.getDefaultValue(argument))
                .filter(value -> !value.isEmpty()).map(value -> "=" + value)
                .orElseGet(() -> variablePickingStrategy.isRequired(argument) ? "!" : "");
        return String.format("$%s:%s%s", variableName, variableType, requiredArgumentPostfix);
    }

    protected String generateOperationArguments(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        String operationArguments = StreamSupport.stream(arguments.spliterator(), false)
                .filter(variablePickingStrategy::isVariable)
                .map(argument -> generateOperationArgument(variablePickingStrategy, argument))
                .collect(Collectors.joining(","));
        return operationArguments.isEmpty() ? "" : "(" + operationArguments + ")";
    }

    protected String generateVariables(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        Map<String, ?> variablesMap = StreamSupport.stream(arguments.spliterator(), false)
                .filter(variablePickingStrategy::isVariable)
                .collect(Collectors.toMap(variablePickingStrategy::getVariableName, GqlParameterValue::getValue));
        if (variablesMap.isEmpty()) {
            return "";
        }
        try {
            return jsonMapper.writeValueAsString(variablesMap);
        } catch (JsonProcessingException jpEx) {
            log.error("Failed to represent variables as JSON.", jpEx);
            return "";
        }
    }

    protected String wrapForRequestBody(String operationBody, String variables) {
        if (variables.isEmpty()) {
            return "{\"query\":\"" + StringUtil.escapeQuotes(operationBody) + "\"}";
        }
        return "{\"variables\":" + variables + ",\"query\":\"" + StringUtil.escapeQuotes(operationBody) + "\"}";
    }
}
