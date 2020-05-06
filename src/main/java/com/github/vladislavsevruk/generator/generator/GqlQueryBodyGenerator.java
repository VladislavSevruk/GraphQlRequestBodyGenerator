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
package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.param.QueryVariable;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Generates body for GraphQL queries for received model according to different field picking strategies.
 */
public class GqlQueryBodyGenerator {

    private static final Logger logger = LogManager.getLogger(GqlQueryBodyGenerator.class);
    private final String queryName;
    private final SelectionSetGenerator selectionSetGenerator;

    public GqlQueryBodyGenerator(String queryName, TypeProvider<?> typeProvider,
            FieldMarkingStrategy fieldMarkingStrategy) {
        this(queryName, typeProvider.getTypeMeta(), fieldMarkingStrategy);
    }

    public GqlQueryBodyGenerator(String queryName, Class<?> model, FieldMarkingStrategy fieldMarkingStrategy) {
        this(queryName, new TypeMeta<>(model), fieldMarkingStrategy);
    }

    private GqlQueryBodyGenerator(String queryName, TypeMeta<?> modelTypeMeta,
            FieldMarkingStrategy fieldMarkingStrategy) {
        this.selectionSetGenerator = new SelectionSetGenerator(modelTypeMeta, fieldMarkingStrategy);
        this.queryName = queryName != null ? queryName : GqlNamePicker.getQueryName(modelTypeMeta.getType());
    }

    /**
     * Builds GraphQL query body with received query variables according to received field picking strategy.
     *
     * @param fieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param queryVariables        <code>QueryVariable</code> vararg with query variable names and values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy, QueryVariable<?>... queryVariables) {
        return generate(fieldsPickingStrategy, Arrays.asList(queryVariables));
    }

    /**
     * Builds GraphQL query body with received query variables according to received field picking strategy.
     *
     * @param fieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param queryVariables        <code>Iterable</code> of <code>QueryVariable</code> with query variable names and
     *                              values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy, Iterable<QueryVariable<?>> queryVariables) {
        Objects.requireNonNull(queryVariables);
        String queryVariablesStr = generateQueryVariables(queryVariables);
        logger.info(() -> String.format("Generating '%s' GraphQL query with%s variables.", queryName,
                queryVariablesStr.isEmpty() ? "out" : " " + queryVariablesStr));
        return "{\"query\":\"{" + queryName + queryVariablesStr + selectionSetGenerator.generate(fieldsPickingStrategy)
                + "}\"}";
    }

    private String addQuotesForStringVariable(String value) {
        return String.format("\\\"%s\\\"", value.replace("\"", "\\\\\\\""));
    }

    private List<String> convertToStringList(Object elements) {
        return createStream(elements)
                .map(value -> CharSequence.class.isAssignableFrom(value.getClass()) ? addQuotesForStringVariable(
                        value.toString()) : value.toString()).collect(Collectors.toList());
    }

    private Stream<?> createStream(Object value) {
        if (Iterable.class.isAssignableFrom(value.getClass())) {
            return StreamSupport.stream(((Iterable<?>) value).spliterator(), false);
        }
        return Arrays.stream((Object[]) value);
    }

    private String generateQueryVariables(Iterable<QueryVariable<?>> variableValue) {
        if (!variableValue.iterator().hasNext()) {
            return "";
        }
        return "(" + StreamSupport.stream(variableValue.spliterator(), false).map(this::performVariableModifications)
                .map(variable -> variable.getName() + ":" + variable.getValue()).collect(Collectors.joining(",")) + ")";
    }

    /**
     * Performs modifications for variables, e.g. - escapes quotes for literals: { "key", "literalValue" } -> { "key",
     * "\"literalValue\"" }, { "key", "literal"With"Quotes" } -> { "key", "\"literal\\\"With\\\"Quotes\"" } - compose
     * iterables or arrays to string: { "key", [ value1, value2 ] } -> { "key", "[value1,value2]" }
     */
    private QueryVariable<?> performVariableModifications(QueryVariable<?> queryVariable) {
        Object value = queryVariable.getValue();
        if (value == null) {
            return queryVariable;
        }
        Class<?> valueClass = value.getClass();
        if (Iterable.class.isAssignableFrom(valueClass) || valueClass.isArray()) {
            // compose all elements through the comma and surround by square brackets
            String modifiedValue = "[" + String.join(",", convertToStringList(value)) + "]";
            return new QueryVariable<>(queryVariable.getName(), modifiedValue);
        }
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            // add escaped quotes for literals
            return new QueryVariable<>(queryVariable.getName(), addQuotesForStringVariable(value.toString()));
        }
        return queryVariable;
    }
}
