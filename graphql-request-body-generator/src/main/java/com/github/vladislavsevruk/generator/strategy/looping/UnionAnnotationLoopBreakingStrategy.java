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

import com.github.vladislavsevruk.generator.annotation.GqlUnionType;

/**
 * Provides loop breaking strategy for excluding looped element from looping sequence based on values from
 * <code>GqlUnionType</code> annotation.
 *
 * @see GqlUnionType
 * @see LoopBreakingStrategy
 */
public class UnionAnnotationLoopBreakingStrategy extends BaseNestingLevelOverrideLoopBreakingStrategy {

    public UnionAnnotationLoopBreakingStrategy(GqlUnionType unionTypeAnnotation,
            LoopBreakingStrategy defaultLoopBreakingStrategy) {
        super(unionTypeAnnotation != null ? unionTypeAnnotation.maxNestingLoopLevel() : -1,
                defaultLoopBreakingStrategy);
    }
}
