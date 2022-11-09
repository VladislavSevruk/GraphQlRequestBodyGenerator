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
package com.github.vladislavsevruk.generator.model.graphql.optimization;

import org.gradle.api.Task;
import org.gradle.api.specs.CompositeSpec;
import org.gradle.api.specs.Spec;

/**
 * A {@link org.gradle.api.specs.CompositeSpec} which requires all its specs to be true in order to evaluate to true.
 * Uses eager evaluation.
 *
 * @param <T> The target type for this Spec.
 */
public class EagerAndSpec<T extends Task> extends CompositeSpec<T> {

    private final Spec<? super T>[] specs;

    @SafeVarargs
    public EagerAndSpec(Spec<? super T>... specs) {
        super(specs);
        this.specs = specs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T element) {
        boolean isAllSatisfied = true;
        for (Spec<? super T> spec : specs) {
            if (!spec.isSatisfiedBy(element)) {
                isAllSatisfied = false;
            }
        }
        return isAllSatisfied;
    }
}
