/*
 * MIT License
 *
 * Copyright (c) 2026 Uladzislau Seuruk
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
package com.github.vladislavsevruk.parsing;

import com.github.vladislavsevruk.parsing.simple.SimpleTokenNode;

/**
 * Initializes specific {@link TokenNode} using provided input withing specified boundaries.
 *
 * @param <T> type of created {@link TokenNode}
 */
@FunctionalInterface
public interface LazyTokenNodeInitializer<T extends TokenNode> {
    /**
     * Initializes specific {@link TokenNode} using provided input withing specified boundaries.
     *
     * @param content    input content that contains are describing specific token node
     * @param startIndex specifies start (inclusive) of the area that describes specific token node
     * @param endIndex   specifies end (exclusive) of the area that describes specific token node
     * @return initialized {@link TokenNode} of specific type
     */
    T initialize(CharSequence content, int startIndex, int endIndex);

    /**
     * Creates instance that returns {@link SimpleTokenNode#empty()} no matter of input.
     *
     * @return lazy token initializer for {@link SimpleTokenNode#empty()}
     */
    static LazyTokenNodeInitializer<TokenNode> empty() {
        return (content, startIndex, endIndex) -> SimpleTokenNode.empty();
    }
}
