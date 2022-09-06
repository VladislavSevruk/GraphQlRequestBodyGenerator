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

import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import com.github.vladislavsevruk.generator.param.GqlDelegateArgument;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.generator.util.ArgumentValueUtil;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Generates GraphQL variable arguments.
 */
@Log4j2
public class GqlVariableArgumentsGenerator extends AbstractGqlVariablesGenerator {

    public GqlVariableArgumentsGenerator(FieldMarkingStrategy inputFieldMarkingStrategy) {
        super(inputFieldMarkingStrategy);
    }

    /**
     * Builds GraphQL operation arguments according to received variable picking strategy.
     *
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> for operation variables generation.
     * @param arguments               <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                values.
     * @return <code>String</code> with resulted GraphQL operation arguments.
     */
    public String generate(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return generate(InputGenerationStrategy.nonNullsFields().getInputFieldsPickingStrategy(),
                variablePickingStrategy, arguments);
    }

    /**
     * Builds GraphQL operation arguments according to received variable picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> to filter required fields for
     *                                   mutation input.
     * @param variablePickingStrategy    <code>VariablePickingStrategy</code> for operation variables generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                   values.
     * @return <code>String</code> with resulted GraphQL operation arguments.
     */
    public String generate(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, Iterable<? extends GqlParameterValue<?>> arguments) {
        String operationArguments = generateOperationArguments(variablePickingStrategy, arguments);
        String delegatedArguments = generateDelegatedArguments(inputFieldsPickingStrategy, arguments);
        operationArguments = combineOperationArguments(operationArguments, delegatedArguments);
        return operationArguments.isEmpty() ? "" : "(" + operationArguments + ")";
    }

    @Override
    protected Object getVariableValue(Object value, Field field, GqlVariableType variableType) {
        Object variableValue = ArgumentValueUtil.getValue(field, value);
        return generateOperationArgument(variableValue, variableType);
    }

    @Override
    protected Object getVariableValue(Object value, Method method, GqlVariableType variableType) {
        Object variableValue = ArgumentValueUtil.getValueByMethod(value, method);
        return generateOperationArgument(variableValue, variableType);
    }

    private String combineOperationArguments(String operationArguments, String delegatedArguments) {
        if (operationArguments.isEmpty()) {
            return delegatedArguments;
        }
        if (delegatedArguments.isEmpty()) {
            return operationArguments;
        }
        return operationArguments + "," + delegatedArguments;
    }

    private String generateDelegatedArguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return StreamSupport.stream(arguments.spliterator(), false).filter(this::isDelegate)
                .map(argument -> (GqlDelegateArgument<?>) argument)
                .map(argument -> collectDelegatedValuesMap(inputFieldsPickingStrategy, argument.getValue(),
                        argument.getValue().getClass(), new LinkedHashMap<>(), argument.isShouldUseVariables()))
                .map(Map::entrySet).flatMap(Collection::stream)
                .map(entry -> generateOperationArgument(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));
    }

    private String generateOperationArgument(VariablePickingStrategy variablePickingStrategy,
            GqlParameterValue<?> argument) {
        String variableName = variablePickingStrategy.getVariableName(argument);
        String variableType = variablePickingStrategy.getVariableType(argument);
        String requiredArgumentPostfix = Optional.ofNullable(variablePickingStrategy.getDefaultValue(argument))
                .filter(value -> !value.isEmpty()).map(value -> "=" + value)
                .orElseGet(() -> variablePickingStrategy.isRequired(argument) ? "!" : "");
        return String.format("$%s:%s%s", variableName, variableType, requiredArgumentPostfix);
    }

    private String generateOperationArgument(String variableName, Object variableType) {
        return String.format("$%s:%s", variableName, variableType);
    }

    private String generateOperationArgument(Object variableValue, GqlVariableType variableTypeAnnotation) {
        String variableType = annotatedArgumentValueExtractor.getVariableType(variableTypeAnnotation, variableValue);
        String requiredArgumentPostfix = Optional.ofNullable(
                        annotatedArgumentValueExtractor.getDefaultValue(variableTypeAnnotation))
                .filter(value -> !value.isEmpty()).map(value -> "=" + value)
                .orElseGet(() -> annotatedArgumentValueExtractor.isRequired(variableTypeAnnotation) ? "!" : "");
        return String.format("%s%s", variableType, requiredArgumentPostfix);
    }

    private String generateOperationArguments(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return StreamSupport.stream(arguments.spliterator(), false).filter(variablePickingStrategy::isVariable)
                .map(argument -> generateOperationArgument(variablePickingStrategy, argument))
                .collect(Collectors.joining(","));
    }

    private boolean isDelegate(GqlParameterValue<?> argument) {
        return GqlDelegateArgument.class.equals(argument.getClass());
    }
}
