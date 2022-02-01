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
package com.github.vladislavsevruk.generator.strategy.looping;

import com.github.vladislavsevruk.resolver.type.TypeMeta;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Provides loop breaking strategy that allows to generate looped items with allowed level of nesting starting from 0
 * to keep only single item at query, e.g. for self-referencing structure <pre>{@code item{id item{...}}}</pre>
 * nesting level 0 (nesting disabled) -
 * <pre>{@code
 * {
 *   id
 * }
 * }</pre>
 * nesting level 1 -
 * <pre>{@code
 * {
 *   id
 *   item {
 *     id
 *   }
 * }
 * }</pre>
 * nesting level 2 -
 * <pre>{@code
 * {
 *   id
 *   item {
 *     id
 *     item {
 *       id
 *     }
 *   }
 * }
 * }</pre>
 */
@Log4j2
public class NestingLoopBreakingStrategy implements LoopBreakingStrategy {

    private final int nestingLoopLevel;

    public NestingLoopBreakingStrategy(int nestingLoopLevel) {
        this.nestingLoopLevel = nestingLoopLevel;
        if (nestingLoopLevel < 0) {
            log.warn("Received nesting loop level at '{}' is '{}' while expected to be greater than or equal to zero.",
                    this.getClass().getName(), nestingLoopLevel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldBreakOnItem(TypeMeta<?> typeMeta, List<TypeMeta<?>> trace) {
        return trace.stream().filter(traceItem -> traceItem.equals(typeMeta)).count() > nestingLoopLevel + 1;
    }
}
