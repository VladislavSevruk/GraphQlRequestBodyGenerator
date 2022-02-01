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
package com.github.vladislavsevruk.generator.strategy.looping;

import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.util.List;

/**
 * Provides loop breaking strategy for picking element on which loop should be broken.
 */
@FunctionalInterface
public interface LoopBreakingStrategy {

    /**
     * Creates new loop breaking strategy that should match both this one's condition and received one's.
     *
     * @param anotherStrategy <code>LoopBreakingStrategy</code> to combine with.
     * @return resulted <code>LoopBreakingStrategy</code>.
     */
    default LoopBreakingStrategy and(LoopBreakingStrategy anotherStrategy) {
        return (fieldTypeMeta, trace) -> this.shouldBreakOnItem(fieldTypeMeta, trace)
                && anotherStrategy.shouldBreakOnItem(fieldTypeMeta, trace);
    }

    /**
     * Creates new loop breaking strategy that should match one of this one's condition or received one's.
     *
     * @param anotherStrategy <code>LoopBreakingStrategy</code> to combine with.
     * @return resulted <code>LoopBreakingStrategy</code>.
     */
    default LoopBreakingStrategy or(LoopBreakingStrategy anotherStrategy) {
        return (fieldTypeMeta, trace) -> this.shouldBreakOnItem(fieldTypeMeta, trace)
                || anotherStrategy.shouldBreakOnItem(fieldTypeMeta, trace);
    }

    /**
     * Checks if loop should be broken on received item.
     *
     * @param typeMeta <code>TypeMeta</code> to check.
     * @param trace    <code>List</code> of <code>TypeMeta</code> with generation item trace.
     * @return <code>true</code> if loop should be broken on received item, <code>false</code> otherwise.
     */
    @SuppressWarnings("java:S1452")
    boolean shouldBreakOnItem(TypeMeta<?> typeMeta, List<TypeMeta<?>> trace);
}
