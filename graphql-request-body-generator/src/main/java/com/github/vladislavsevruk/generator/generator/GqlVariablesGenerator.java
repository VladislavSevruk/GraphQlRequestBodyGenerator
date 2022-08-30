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
package com.github.vladislavsevruk.generator.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import com.github.vladislavsevruk.generator.param.GqlDelegateArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.ArgumentValueUtil;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates GraphQL operation variables.
 */
@Log4j2
public class GqlVariablesGenerator extends AbstractGqlVariablesGenerator {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public GqlVariablesGenerator(FieldMarkingStrategy inputFieldMarkingStrategy) {
        super(inputFieldMarkingStrategy);
    }

    /**
     * Builds GraphQL operation variables according to received variable picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param variablePickingStrategy    <code>VariablePickingStrategy</code> for operation variables generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                   values.
     * @return <code>String</code> .
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, Iterable<? extends GqlParameterValue<?>> arguments) {
        Map<String, Object> variablesMap = getVariablesMap(variablePickingStrategy, arguments);
        Map<String, Object> delegatedVariablesMap = getDelegatedVariablesMap(inputFieldsPickingStrategy, arguments);
        validateNoDuplicates(variablesMap, delegatedVariablesMap);
        variablesMap.putAll(delegatedVariablesMap);
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

    @Override
    protected Object getVariableValue(Object value, Field field, GqlVariableType variableType) {
        return ArgumentValueUtil.getValue(field, value);
    }

    @Override
    protected Object getVariableValue(Object value, Method method, GqlVariableType variableType) {
        return ArgumentValueUtil.getValueByMethod(value, method);
    }

    private static IllegalStateException newDuplicateKeyException(String key) {
        return new IllegalStateException("Duplicated key: " + key);
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> { throw newDuplicateKeyException(((GqlParameterValue<?>) u).getName()); };
    }

    private Map<String, Object> getDelegatedVariablesMap(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return StreamSupport.stream(arguments.spliterator(), false).filter(this::isDelegate)
                .map(GqlParameterValue::getValue)
                .map(value -> collectDelegatedValuesMap(inputFieldsPickingStrategy, value, value.getClass(),
                        new LinkedHashMap<>())).map(Map::entrySet).flatMap(Collection::stream)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, throwingMerger(), LinkedHashMap::new));
    }

    private Map<String, Object> getVariablesMap(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return StreamSupport.stream(arguments.spliterator(), false).filter(variablePickingStrategy::isVariable).collect(
                Collectors.toMap(variablePickingStrategy::getVariableName, GqlParameterValue::getValue,
                        throwingMerger(), LinkedHashMap::new));
    }

    private boolean isDelegate(GqlParameterValue<?> argument) {
        return GqlDelegateArgument.class.equals(argument.getClass());
    }

    private void validateNoDuplicates(Map<String, ?> variablesMap, Map<String, ?> delegatedVariablesMap) {
        Set<String> variableKeys = variablesMap.keySet();
        for (String key : delegatedVariablesMap.keySet()) {
            if (variableKeys.contains(key)) {
                throw newDuplicateKeyException(key);
            }
        }
    }
}
