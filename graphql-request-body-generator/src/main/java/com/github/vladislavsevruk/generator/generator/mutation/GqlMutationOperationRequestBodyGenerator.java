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

import com.github.vladislavsevruk.generator.generator.GqlOperationRequestBodyGenerator;
import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;

import java.util.Arrays;

/**
 * Abstract request body generator with common logic for GraphQL mutation operation.
 *
 * @param <T> self type to return subclass type at chain methods.
 */
public abstract class GqlMutationOperationRequestBodyGenerator<T extends GqlMutationOperationRequestBodyGenerator<T>>
        extends GqlOperationRequestBodyGenerator<T> {

    protected InputFieldsPickingStrategy inputFieldsPickingStrategy = InputGenerationStrategy.defaultStrategy()
            .getInputFieldsPickingStrategy();
    protected ModelArgumentStrategy modelArgumentStrategy = ModelArgumentGenerationStrategy.defaultStrategy()
            .getModelArgumentStrategy();

    public GqlMutationOperationRequestBodyGenerator(String mutationName) {
        super(mutationName);
    }

    /**
     * Adds arguments to GraphQL operation with default mutation input fields picking strategy.
     *
     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    @Override
    public T arguments(GqlParameterValue<?>... arguments) {
        return arguments(InputGenerationStrategy.defaultStrategy(), ModelArgumentGenerationStrategy.defaultStrategy(),
                arguments);
    }

    /**
     * Adds arguments to GraphQL operation with default mutation input fields picking strategy.
     *
     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
     * @return this.
     */
    @Override
    public T arguments(Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(InputGenerationStrategy.defaultStrategy(), ModelArgumentGenerationStrategy.defaultStrategy(),
                arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy <code>InputGenerationStrategy</code> with predefined fields picking strategy for
     *                                mutation input generation.
     * @param arguments               <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            GqlParameterValue<?>... arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy <code>InputGenerationStrategy</code> with predefined fields picking strategy for
     *                                mutation input generation.
     * @param arguments               <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy for mutation
     *                                   input generation.
     * @param arguments                  <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            GqlParameterValue<?>... arguments) {
        return arguments(inputFieldsPickingStrategy, Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy for mutation
     *                                   input generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names
     *                                   and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(inputFieldsPickingStrategy,
                ModelArgumentGenerationStrategy.defaultStrategy().getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received predefined mutation input fields picking strategy.
     *
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(ModelArgumentGenerationStrategy modelArgumentGenerationStrategy,
            GqlParameterValue<?>... arguments) {
        return arguments(modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received predefined mutation input fields picking strategy.
     *
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                        names and values.
     * @return this.
     */
    public T arguments(ModelArgumentGenerationStrategy modelArgumentGenerationStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with received mutation input fields picking strategy.
     *
     * @param modelArgumentStrategy <code>ModelArgumentStrategy</code> model argument treating strategy for mutation
     *                              argument generation.
     * @param arguments             <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(ModelArgumentStrategy modelArgumentStrategy,
            GqlParameterValue<?>... arguments) {
        return arguments(modelArgumentStrategy, Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param modelArgumentStrategy <code>ModelArgumentStrategy</code> model argument treating strategy for mutation
     *                              argument generation.
     * @param arguments             <code>Iterable</code> of <code>GqlParameterValue</code> with argument names
     *                              and values.
     * @return this.
     */
    public T arguments(ModelArgumentStrategy modelArgumentStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(InputGenerationStrategy.defaultStrategy().getInputFieldsPickingStrategy(),
                modelArgumentStrategy, arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy         <code>InputGenerationStrategy</code> with predefined fields picking
     *                                        strategy for mutation input generation.
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            ModelArgumentGenerationStrategy modelArgumentGenerationStrategy, GqlParameterValue<?>... arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(),
                modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy         <code>InputGenerationStrategy</code> with predefined fields picking
     *                                        strategy for mutation input generation.
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                        names and values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            ModelArgumentGenerationStrategy modelArgumentGenerationStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(),
                modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy for mutation
     *                                   input generation.
     * @param modelArgumentStrategy      <code>ModelArgumentStrategy</code> model argument treating strategy for
     *                                   mutation argument generation.
     * @param arguments                  <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, GqlParameterValue<?>... arguments) {
        return arguments(inputFieldsPickingStrategy, modelArgumentStrategy, Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy for mutation
     *                                   input generation.
     * @param modelArgumentStrategy      <code>ModelArgumentStrategy</code> model argument treating strategy for
     *                                   mutation argument generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names
     *                                   and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            ModelArgumentStrategy modelArgumentStrategy, Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(inputFieldsPickingStrategy,
                VariableGenerationStrategy.defaultStrategy().getVariablePickingStrategy(), modelArgumentStrategy,
                arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy         <code>InputGenerationStrategy</code> with predefined fields picking
     *                                        strategy for mutation input generation.
     * @param variableGenerationStrategy      <code>VariableGenerationStrategy</code> variables picking strategy for
     *                                        variables generation.
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            VariableGenerationStrategy variableGenerationStrategy,
            ModelArgumentGenerationStrategy modelArgumentGenerationStrategy, GqlParameterValue<?>... arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(),
                variableGenerationStrategy.getVariablePickingStrategy(),
                modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputGenerationStrategy         <code>InputGenerationStrategy</code> with predefined fields picking
     *                                        strategy for mutation input generation.
     * @param variableGenerationStrategy      <code>VariableGenerationStrategy</code> variables picking strategy for
     *                                        variables generation.
     * @param modelArgumentGenerationStrategy <code>ModelArgumentGenerationStrategy</code> with predefined arguments
     *                                        treating strategy for mutation argument generation.
     * @param arguments                       <code>Iterable</code> of <code>GqlParameterValue</code> with argument
     *                                        names and values.
     * @return this.
     */
    public T arguments(InputGenerationStrategy inputGenerationStrategy,
            VariableGenerationStrategy variableGenerationStrategy,
            ModelArgumentGenerationStrategy modelArgumentGenerationStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(inputGenerationStrategy.getInputFieldsPickingStrategy(),
                variableGenerationStrategy.getVariablePickingStrategy(),
                modelArgumentGenerationStrategy.getModelArgumentStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy for mutation
     *                                   input generation.
     * @param variablePickingStrategy    <code>VariablePickingStrategy</code> variables picking strategy for
     *                                   variables generation.
     * @param modelArgumentStrategy      <code>ModelArgumentStrategy</code> model argument treating strategy for
     *                                   mutation argument generation.
     * @param arguments                  <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, ModelArgumentStrategy modelArgumentStrategy,
            GqlParameterValue<?>... arguments) {
        return arguments(inputFieldsPickingStrategy, variablePickingStrategy, modelArgumentStrategy,
                Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation with predefined mutation input fields picking strategy.
     *
     * @param inputFieldsPickingStrategy <code>InputFieldsPickingStrategy</code> fields picking strategy to get
     *                                   input type.
     * @param variablePickingStrategy    <code>VariablePickingStrategy</code> variables picking strategy for
     *                                   variables generation.
     * @param modelArgumentStrategy      <code>ModelArgumentStrategy</code> model argument treating strategy for
     *                                   mutation argument generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names
     *                                   and values.
     * @return this.
     */
    public T arguments(InputFieldsPickingStrategy inputFieldsPickingStrategy,
            VariablePickingStrategy variablePickingStrategy, ModelArgumentStrategy modelArgumentStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        super.arguments(variablePickingStrategy, arguments);
        this.inputFieldsPickingStrategy = inputFieldsPickingStrategy;
        this.modelArgumentStrategy = modelArgumentStrategy;
        return thisInstance();
    }

    @Override
    protected void setExtendedArgumentsStrategiesToDefault() {
        inputFieldsPickingStrategy = InputGenerationStrategy.defaultStrategy().getInputFieldsPickingStrategy();
        modelArgumentStrategy = ModelArgumentGenerationStrategy.defaultStrategy().getModelArgumentStrategy();
    }
}
