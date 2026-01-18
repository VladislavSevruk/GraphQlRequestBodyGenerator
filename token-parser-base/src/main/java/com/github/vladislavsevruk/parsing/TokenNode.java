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

import static com.github.vladislavsevruk.parsing.simple.SimpleTokenNode.empty;

/**
 * Represents outcome of some input token parsing process. May either have some intermediate state and support partial
 * parsing or be logically indivisible. Instances with intermediate states support are able to resume parsing after
 * provision of additional input.
 */
public interface TokenNode {
    /**
     * Resumes parsing of current node from the very beginning of received input. Parsing can be resumed only if current
     * node supports partial parsing with intermediate states is not already in final state. {@link #isCompleted()}
     * method can be used to check whether current node is already completed and cannot resume parsing.
     *
     * @param content      next input to process
     * @param hasMoreInput identifies if additional input can be provided by request
     * @return parsing result of provided input from the very beginning
     * @throws IllegalStateException         if parsing of this node is already completed
     * @throws UnsupportedOperationException if node cannot have intermediate states and doesn't support partial parsing
     */
    default TokenParsingResult resumeParsing(CharSequence content, boolean hasMoreInput) {
        return resumeParsing(content, 0, hasMoreInput);
    }

    /**
     * Resumes parsing of current node starting from received position. Parsing can be resumed only if current node
     * supports partial parsing with intermediate states is not already in final state. {@link #isCompleted()} method
     * can be used to check whether current node is already completed and cannot resume parsing.
     *
     * @param content      next input to process
     * @param startIndex   position (inclusive) of input to resume parsing from
     * @param hasMoreInput identifies if additional input can be provided by request
     * @return parsing result of provided input from received start position
     * @throws IllegalStateException         if parsing of this node is already completed
     * @throws UnsupportedOperationException if node cannot have intermediate states and doesn't support partial parsing
     */
    TokenParsingResult resumeParsing(CharSequence content, int startIndex, boolean hasMoreInput);

    /**
     * Identifies if parsing of current node is completed, and it can be considered as reached final state.
     *
     * @return {@code true} if parsing of value of this node is finished, {@code false} if it's still in progress
     */
    boolean isCompleted();

    /**
     * Identifies whether this node is empty and has no significant value or not.
     *
     * @return {@code true} if this node is empty, {@code false} otherwise
     */
    default boolean isEmpty() {
        return this == empty();
    }
}
