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

import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.SelectionSetGenerationStrategy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * Abstract request body generator with common logic for GraphQL operation.
 *
 * @param <T> self type to return subclass type at chain methods.
 */
public abstract class GqlOperationRequestBodyGenerator<T extends GqlOperationRequestBodyGenerator> {

    @Getter(AccessLevel.PROTECTED)
    private Iterable<? extends GqlParameterValue<?>> arguments = Collections.emptyList();
    @Getter(AccessLevel.PROTECTED)
    private String operationName;
    @Getter(AccessLevel.PROTECTED)
    private FieldsPickingStrategy selectionSetFieldsPickingStrategy = SelectionSetGenerationStrategy.defaultStrategy()
            .getFieldsPickingStrategy();
    private TypeMeta<?> selectionSetTypeMeta;

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
        this.arguments = arguments;
        return thisInstance();
    }

    /**
     * Returns generated GraphQL operation body with predefined parameters.
     */
    public abstract String generate();

    /**
     * Sets selection set model for GraphQL operation with default selection set fields picking strategy.
     *
     * @param model <code>Class</code> of model that will be used for selection set generation.
     * @return this.
     */
    public T selectionSet(Class<?> model) {
        return selectionSet(model, SelectionSetGenerationStrategy.defaultStrategy());
    }

    /**
     * Sets selection set model for GraphQL operation with default selection set fields picking strategy.
     *
     * @param typeProvider <code>TypeProvider</code> with model reference that will be used for selection set
     *                     generation.
     * @return this.
     */
    public T selectionSet(TypeProvider<?> typeProvider) {
        return selectionSet(typeProvider, SelectionSetGenerationStrategy.defaultStrategy());
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
     * @param selectionSetFieldsPickingStrategy <code>FieldsPickingStrategy</code> fields picking strategy for
     *                                          selection set generation.
     * @return this.
     */
    public T selectionSet(Class<?> model, FieldsPickingStrategy selectionSetFieldsPickingStrategy) {
        this.selectionSetFieldsPickingStrategy = selectionSetFieldsPickingStrategy;
        return selectionSet(new TypeMeta<>(model));
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
        this.selectionSetFieldsPickingStrategy = selectionSetGenerationStrategy.getFieldsPickingStrategy();
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
        this.selectionSetFieldsPickingStrategy = selectionSetFieldsPickingStrategy;
        return selectionSet(typeProvider.getTypeMeta());
    }

    protected SelectionSetGenerator getSelectionSetGenerator() {
        Objects.requireNonNull(selectionSetTypeMeta, "Selection set model wasn't set.");
        return new SelectionSetGenerator(selectionSetTypeMeta,
                FieldMarkingStrategySourceManager.selectionSet().getStrategy());
    }

    private T selectionSet(TypeMeta<?> typeMeta) {
        this.selectionSetTypeMeta = typeMeta;
        return thisInstance();
    }

    @SuppressWarnings("unchecked")
    private T thisInstance() {
        return (T) this;
    }
}
