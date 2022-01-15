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
package com.github.vladislavsevruk.generator.strategy.looping;

import lombok.Getter;

/**
 * Contains predefined {@link LoopBreakingStrategy} for selection set:<ul>
 * <li> break on first element of looping sequence
 * </ul>
 *
 * @see LoopBreakingStrategy
 * @see DefaultLoopBreakingStrategy
 */
public enum EndlessLoopBreakingStrategy {

    EXCLUDE_FIRST_ENTRY(new DefaultLoopBreakingStrategy());

    @Getter
    private LoopBreakingStrategy loopBreakingStrategy;

    EndlessLoopBreakingStrategy(LoopBreakingStrategy loopBreakingStrategy) {
        this.loopBreakingStrategy = loopBreakingStrategy;
    }

    /**
     * Returns default loop breaking strategy.
     */
    public static EndlessLoopBreakingStrategy defaultStrategy() {
        return excludeFirstEntry();
    }

    /**
     * Returns strategy for loop breaking excluding first looped element of looping sequence.
     */
    public static EndlessLoopBreakingStrategy excludeFirstEntry() {
        return EXCLUDE_FIRST_ENTRY;
    }
}
