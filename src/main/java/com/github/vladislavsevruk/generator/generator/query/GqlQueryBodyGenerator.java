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
package com.github.vladislavsevruk.generator.generator.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladislavsevruk.generator.generator.GqlBodyGenerator;
import com.github.vladislavsevruk.generator.generator.SelectionSetGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.input.type.InputTypePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * @param fieldsPickingStrategy    <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param inputTypePickingStrategy <code>InputTypePickingStrategy</code> for query with input type.
     * @param arguments                <code>GqlArgument</code> varargs with argument names and values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy, InputTypePickingStrategy inputTypePickingStrategy,
                           GqlParameterValue<?>... arguments) {
        return generate(fieldsPickingStrategy, inputTypePickingStrategy, Arrays.asList(arguments));
    }

    /**
     * Builds GraphQL query body with received arguments according to received field picking strategy.
     *
     * @param fieldsPickingStrategy    <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param inputTypePickingStrategy <code>InputTypePickingStrategy</code> for query with input type.
     * @param arguments                <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                 values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy,
                           InputTypePickingStrategy inputTypePickingStrategy,
                           Iterable<? extends GqlParameterValue<?>> arguments) {
        Objects.requireNonNull(arguments);
        String selectionSet = selectionSetGenerator.generate(fieldsPickingStrategy);
        String signature = getSignature(inputTypePickingStrategy, arguments);
        log.info(() -> String.format("Generating '%s' GraphQL query.", queryName));
        String wrappedQuery;
        if (StringUtils.isEmpty(signature)) {
            String query = "{" + queryName + generateGqlArguments(arguments) + selectionSet + "}";
            wrappedQuery = wrapForRequestBody(query);
        } else {
            String query = "query" + signature + "{" + queryName + generateGqlArgumentsWithInputType(arguments) + selectionSet + "}";
            wrappedQuery = wrapForRequestBodyWithInputType(query, generateVariables(arguments));
        }
        log.debug(() -> "Resulted query: " + wrappedQuery);
        return wrappedQuery;
    }

    private String generateGqlArguments(Iterable<? extends GqlParameterValue<?>> argumentValue) {
        if (!argumentValue.iterator().hasNext()) {
            log.debug("GraphQL query argument iterable is empty.");
            return "";
        }
        return "(" + StreamSupport.stream(argumentValue.spliterator(), false)
                .map(argument -> argument.getName() + ":" + StringUtil.generateEscapedValueString(argument.getValue()))
                .collect(Collectors.joining(",")) + ")";
    }

    private String generateVariables(Iterable<? extends GqlParameterValue<?>> arguments) {
        Objects.requireNonNull(arguments);
        return generateGqlVariables(arguments).stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    private List<String> generateGqlVariables(Iterable<? extends GqlParameterValue<?>> arguments) {
        List<String> variables = new ArrayList<>();
        for (GqlParameterValue<?> next : arguments) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                variables.add(objectMapper.writeValueAsString(next.getValue()));
            } catch (JsonProcessingException exception) {
                log.error(() -> "Unable to parse JSON by argument object", exception);
            }
        }
        return variables;
    }
}
