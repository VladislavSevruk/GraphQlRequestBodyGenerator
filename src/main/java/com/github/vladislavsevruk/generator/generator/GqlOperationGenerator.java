/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2020 Uladzislau Seuruk
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
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
 * TODO
 *
 * @param <T>
 */
@Getter(AccessLevel.PROTECTED)
public abstract class GqlOperationGenerator<T extends GqlOperationGenerator> {

    private Iterable<? extends GqlParameterValue<?>> arguments = Collections.emptyList();
    private String operationName;
    private FieldsPickingStrategy selectionSetFieldsPickingStrategy = SelectionSetGenerationStrategy.defaultStrategy()
            .getFieldsPickingStrategy();
    private SelectionSetGenerator selectionSetGenerator;

    protected GqlOperationGenerator(String operationName) {
        this.operationName = operationName;
    }

    /**
     * TODO
     */
    public T arguments(GqlParameterValue<?>... arguments) {
        return arguments(Arrays.asList(arguments));
    }

    /**
     * TODO
     */
    public T arguments(Iterable<? extends GqlParameterValue<?>> arguments) {
        this.arguments = arguments;
        return thisInstance();
    }

    /**
     * TODO
     */
    public abstract String generate();

    /**
     * TODO
     */
    public T selectionSet(Class<?> model, SelectionSetGenerationStrategy selectionSetGenerationStrategy) {
        return selectionSet(model, selectionSetGenerationStrategy.getFieldsPickingStrategy());
    }

    /**
     * TODO
     */
    public T selectionSet(Class<?> model, FieldsPickingStrategy selectionSetFieldsPickingStrategy) {
        this.selectionSetFieldsPickingStrategy = selectionSetFieldsPickingStrategy;
        return selectionSet(model);
    }

    /**
     * TODO
     */
    public T selectionSet(TypeProvider<?> typeProvider) {
        return selectionSet(typeProvider.getTypeMeta());
    }

    /**
     * TODO
     */
    public T selectionSet(TypeProvider<?> typeProvider, SelectionSetGenerationStrategy selectionSetGenerationStrategy) {
        this.selectionSetFieldsPickingStrategy = selectionSetGenerationStrategy.getFieldsPickingStrategy();
        return selectionSet(typeProvider, selectionSetGenerationStrategy.getFieldsPickingStrategy());
    }

    /**
     * TODO
     */
    public T selectionSet(TypeProvider<?> typeProvider, FieldsPickingStrategy selectionSetFieldsPickingStrategy) {
        this.selectionSetFieldsPickingStrategy = selectionSetFieldsPickingStrategy;
        return selectionSet(typeProvider);
    }

    /**
     * TODO
     */
    public T selectionSet(Class<?> model) {
        return selectionSet(new TypeMeta<>(model));
    }

    protected void verifySelectionSet() {
        Objects.requireNonNull(selectionSetGenerator, "Selection set model wasn't set.");
    }

    private T selectionSet(TypeMeta<?> typeMeta) {
        selectionSetGenerator = new SelectionSetGenerator(typeMeta,
                FieldMarkingStrategySourceManager.selectionSet().getStrategy());
        return thisInstance();
    }

    @SuppressWarnings("unchecked")
    private T thisInstance() {
        return (T) this;
    }
}
