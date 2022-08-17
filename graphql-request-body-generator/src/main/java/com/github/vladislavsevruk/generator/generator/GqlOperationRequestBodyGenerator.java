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
package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.looping.EndlessLoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.looping.LoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.SelectionSetGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariablePickingStrategy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract request body generator with common logic for GraphQL operation.
 *
 * @param <T> self type to return subclass type at chain methods.
 */
public abstract class GqlOperationRequestBodyGenerator<T extends GqlOperationRequestBodyGenerator<T>> {

    @Getter(AccessLevel.PROTECTED)
    private Iterable<? extends GqlParameterValue<?>> arguments = Collections.emptyList();
    private LoopBreakingStrategy loopBreakingStrategy = EndlessLoopBreakingStrategy.defaultStrategy()
            .getLoopBreakingStrategy();
    @Getter(AccessLevel.PROTECTED)
    private String operationAlias;
    @Getter(AccessLevel.PROTECTED)
    private final String operationName;
    @Getter(AccessLevel.PROTECTED)
    private FieldsPickingStrategy selectionSetFieldsPickingStrategy = SelectionSetGenerationStrategy.defaultStrategy()
            .getFieldsPickingStrategy();
    private TypeMeta<?> selectionSetTypeMeta;
    @Getter(AccessLevel.PROTECTED)
    private VariablePickingStrategy variablePickingStrategy = VariableGenerationStrategy.defaultStrategy()
            .getVariablePickingStrategy();

    protected GqlOperationRequestBodyGenerator(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Adds arguments to GraphQL operation.
     *
     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(GqlParameterValue<?>... arguments) {
        return arguments(Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation.
     *
     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
     * @return this.
     */
    public T arguments(Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(VariableGenerationStrategy.defaultStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined variable generation strategy.
     *
     * @param variableGenerationStrategy <code>VariableGenerationStrategy</code> variables picking strategy for
     *                                   variables generation.
     * @param arguments                  <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(VariableGenerationStrategy variableGenerationStrategy, GqlParameterValue<?>... arguments) {
        return arguments(variableGenerationStrategy.getVariablePickingStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined variable generation strategy.
     *
     * @param variableGenerationStrategy <code>VariableGenerationStrategy</code> variables picking strategy for
     *                                   variables generation.
     * @param arguments                  <code>Iterable</code> of <code>GqlParameterValue</code> with argument names
     *                                   and values.
     * @return this.
     */
    public T arguments(VariableGenerationStrategy variableGenerationStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        return arguments(variableGenerationStrategy.getVariablePickingStrategy(), arguments);
    }

    /**
     * Adds arguments to GraphQL operation with predefined variable picking strategy.
     *
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> variables picking strategy for variables
     *                                generation.
     * @param arguments               <code>GqlParameterValue</code> varargs with argument names and values.
     * @return this.
     */
    public T arguments(VariablePickingStrategy variablePickingStrategy, GqlParameterValue<?>... arguments) {
        return arguments(variablePickingStrategy, Arrays.asList(arguments));
    }

    /**
     * Adds arguments to GraphQL operation with predefined variable picking strategy.
     *
     * @param variablePickingStrategy <code>VariablePickingStrategy</code> variables picking strategy for variables
     *                                generation.
     * @param arguments               <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and
     *                                values.
     * @return this.
     */
    public T arguments(VariablePickingStrategy variablePickingStrategy,
            Iterable<? extends GqlParameterValue<?>> arguments) {
        setExtendedArgumentsStrategiesToDefault();
        this.arguments = arguments;
        this.variablePickingStrategy = variablePickingStrategy;
        return thisInstance();
    }

    /**
     * Returns generated GraphQL operation body with predefined parameters.
     */
    public abstract String generate();

    /**
     * Sets operation alias for GraphQL operation.
     *
     * @param operationAlias <code>String</code> with alias that should be used for GraphQL operation generation.
     * @return this.
     */
    public T operationAlias(String operationAlias) {
        this.operationAlias = operationAlias;
        return thisInstance();
    }

    /**
     * Sets selection set model for GraphQL operation with default selection set fields picking strategy.
     *
     * @param model <code>Class</code> of model that will be used for selection set generation.
     * @return this.
     */
    public T selectionSet(Class<?> model) {
        return selectionSet(model, SelectionSetGenerationStrategy.defaultStrategy(),
                EndlessLoopBreakingStrategy.defaultStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with default selection set fields picking strategy.
     *
     * @param typeProvider <code>TypeProvider</code> with model reference that will be used for selection set
     *                     generation.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider) {
        return selectionSet(typeProvider, SelectionSetGenerationStrategy.defaultStrategy(),
                EndlessLoopBreakingStrategy.defaultStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received predefined selection set fields picking strategy.
     *
     * @param model                          <code>Class</code> of model that will be used for selection set
     *                                       generation.
     * @param selectionSetGenerationStrategy <code>SelectionSetGenerationStrategy</code> with predefined fields picking
     *                                       strategy for selection set generation.
     * @return this.
     */
    public T selectionSet(Class<?> model, SelectionSetGenerationStrategy selectionSetGenerationStrategy) {
        return selectionSet(model, selectionSetGenerationStrategy.getFieldsPickingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param model                             <code>Class</code> of model that will be used for selection set
     *                                          generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> for selection set generation.
     * @return this.
     */
    public T selectionSet(Class<?> model, FieldsPickingStrategy selectionSetFieldsPickingStrategy) {
        return selectionSet(model, selectionSetFieldsPickingStrategy,
                EndlessLoopBreakingStrategy.defaultStrategy().getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param model                       <code>Class</code> of model that will be used for selection set
     *                                    generation.
     * @param endlessLoopBreakingStrategy <code>EndlessLoopBreakingStrategy</code> with predefined loop breaking
     *                                    strategy for picking element on which loop should be broken.
     * @return this.
     */
    public T selectionSet(Class<?> model, EndlessLoopBreakingStrategy endlessLoopBreakingStrategy) {
        return selectionSet(model, endlessLoopBreakingStrategy.getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param model                <code>Class</code> of model that will be used for selection set
     *                             generation.
     * @param loopBreakingStrategy <code>LoopBreakingStrategy</code> for picking element on which loop should be
     *                             broken.
     * @return this.
     */
    public T selectionSet(Class<?> model, LoopBreakingStrategy loopBreakingStrategy) {
        return selectionSet(model, SelectionSetGenerationStrategy.defaultStrategy().getFieldsPickingStrategy(),
                loopBreakingStrategy);
    }

    /**
     * Sets selection set model for GraphQL operation with received predefined selection set fields picking strategy.
     *
     * @param typeProvider                   <code>TypeProvider</code> with model reference that will be used for
     *                                       selection set generation.
     * @param selectionSetGenerationStrategy <code>SelectionSetGenerationStrategy</code> with predefined fields picking
     *                                       strategy for selection set generation.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, SelectionSetGenerationStrategy selectionSetGenerationStrategy) {
        return selectionSet(typeProvider, selectionSetGenerationStrategy.getFieldsPickingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param typeProvider                      <code>TypeProvider</code> with model reference that will be used for
     *                                          selection set generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> fields picking strategy for
     *                                          selection set generation.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, FieldsPickingStrategy selectionSetFieldsPickingStrategy) {
        return selectionSet(typeProvider, selectionSetFieldsPickingStrategy,
                EndlessLoopBreakingStrategy.defaultStrategy().getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received predefined selection set fields picking strategy.
     *
     * @param typeProvider                <code>TypeProvider</code> with model reference that will be used for
     *                                    selection set generation.
     * @param endlessLoopBreakingStrategy <code>EndlessLoopBreakingStrategy</code> with predefined loop breaking
     *                                    strategy for picking element on which loop should be broken.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, EndlessLoopBreakingStrategy endlessLoopBreakingStrategy) {
        return selectionSet(typeProvider, endlessLoopBreakingStrategy.getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param typeProvider         <code>TypeProvider</code> with model reference that will be used for
     *                             selection set generation.
     * @param loopBreakingStrategy <code>LoopBreakingStrategy</code> for picking element on which loop should be
     *                             broken.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, LoopBreakingStrategy loopBreakingStrategy) {
        return selectionSet(typeProvider, SelectionSetGenerationStrategy.defaultStrategy().getFieldsPickingStrategy(),
                loopBreakingStrategy);
    }

    /**
     * Sets selection set model for GraphQL operation with received predefined selection set fields picking strategy.
     *
     * @param model                          <code>Class</code> of model that will be used for selection set
     *                                       generation.
     * @param selectionSetGenerationStrategy <code>SelectionSetGenerationStrategy</code> with predefined fields picking
     *                                       strategy for selection set generation.
     * @param endlessLoopBreakingStrategy    <code>EndlessLoopBreakingStrategy</code> with predefined loop breaking
     *                                       strategy for picking element on which loop should be broken.
     * @return this.
     */
    public T selectionSet(Class<?> model, SelectionSetGenerationStrategy selectionSetGenerationStrategy,
            EndlessLoopBreakingStrategy endlessLoopBreakingStrategy) {
        return selectionSet(model, selectionSetGenerationStrategy.getFieldsPickingStrategy(),
                endlessLoopBreakingStrategy.getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param model                             <code>Class</code> of model that will be used for selection set
     *                                          generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> fields picking strategy for
     *                                          selection set generation.
     * @param loopBreakingStrategy              <code>LoopBreakingStrategy</code> for picking element on which loop
     *                                          should be broken.
     * @return this.
     */
    public T selectionSet(Class<?> model, FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            LoopBreakingStrategy loopBreakingStrategy) {
        return selectionSet(new TypeMeta<>(model), selectionSetFieldsPickingStrategy, loopBreakingStrategy);
    }

    /**
     * Sets selection set model for GraphQL operation with received predefined selection set fields picking strategy.
     *
     * @param typeProvider                   <code>TypeProvider</code> with model reference that will be used for
     *                                       selection set generation.
     * @param selectionSetGenerationStrategy <code>SelectionSetGenerationStrategy</code> with predefined fields picking
     *                                       strategy for selection set generation.
     * @param endlessLoopBreakingStrategy    <code>EndlessLoopBreakingStrategy</code> with predefined loop breaking
     *                                       strategy for picking element on which loop should be broken.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, SelectionSetGenerationStrategy selectionSetGenerationStrategy,
            EndlessLoopBreakingStrategy endlessLoopBreakingStrategy) {
        return selectionSet(typeProvider, selectionSetGenerationStrategy.getFieldsPickingStrategy(),
                endlessLoopBreakingStrategy.getLoopBreakingStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with received selection set fields picking strategy.
     *
     * @param typeProvider                      <code>TypeProvider</code> with model reference that will be used for
     *                                          selection set generation.
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> fields picking strategy for
     *                                          selection set generation.
     * @param loopBreakingStrategy              <code>LoopBreakingStrategy</code> for picking element on which loop
     *                                          should be broken.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider, FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            LoopBreakingStrategy loopBreakingStrategy) {
        return selectionSet(typeProvider.getTypeMeta(), selectionSetFieldsPickingStrategy, loopBreakingStrategy);
    }

    protected SelectionSetGenerator getSelectionSetGenerator() {
        Objects.requireNonNull(selectionSetTypeMeta, "Selection set model wasn't set.");
        return new SelectionSetGenerator(selectionSetTypeMeta,
                FieldMarkingStrategySourceManager.selectionSet().getStrategy(), loopBreakingStrategy);
    }

    // can be overridden at descendants to reset strategies to default when methods for argument modification from this class are called
    protected void setExtendedArgumentsStrategiesToDefault() {
    }

    private <U> U orDefault(U value, Supplier<U> defaultValue) {
        if (value != null) {
            return value;
        }
        return defaultValue.get();
    }

    private T selectionSet(TypeMeta<?> typeMeta, FieldsPickingStrategy selectionSetFieldsPickingStrategy,
            LoopBreakingStrategy loopBreakingStrategy) {
        this.selectionSetTypeMeta = typeMeta;
        this.selectionSetFieldsPickingStrategy = orDefault(selectionSetFieldsPickingStrategy,
                SelectionSetGenerationStrategy.defaultStrategy()::getFieldsPickingStrategy);
        this.loopBreakingStrategy = orDefault(loopBreakingStrategy,
                EndlessLoopBreakingStrategy.defaultStrategy()::getLoopBreakingStrategy);
        return thisInstance();
    }

    @SuppressWarnings("unchecked")
    protected final T thisInstance() {
        return (T) this;
    }
}
