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
package com.github.vladislavsevruk.parsing.simple;

import com.github.vladislavsevruk.parsing.TokenNode;
import com.github.vladislavsevruk.parsing.TokenParsingResult;

/**
 * Represents token node with simple non-divisible result that cannot have partial result or intermediate state.
 */
public class SimpleTokenNode implements TokenNode {
    private static final TokenNode EMPTY_INSTANCE = new SimpleTokenNode();
    private static final TokenNode NULL_INSTANCE = new SimpleTokenNode();

    protected SimpleTokenNode() {
    }

    /**
     * Returns special empty instance to indicate that token was present but has no significant value for parsing
     * result.
     *
     * @return special empty instance
     */
    public static TokenNode empty() {
        return EMPTY_INSTANCE;
    }

    /**
     * Returns special null instance to indicate that token was present and had special placeholder value for missing
     * information used at various languages and formats.
     *
     * @return special null instance
     */
    public static TokenNode nullValue() {
        return NULL_INSTANCE;
    }

    /**
     * This implementation throws exception on attempt to resume parsing since it's not expected to have any partial
     * results or intermediate states.
     *
     * @param content      next input to process
     * @param startIndex   position (inclusive) of input to resume parsing from
     * @param hasMoreInput identifies if additional input can be provided by request
     * @return never, nothing
     * @throws UnsupportedOperationException as such token cannot have intermediate state
     */
    @Override
    public TokenParsingResult resumeParsing(CharSequence content, int startIndex, boolean hasMoreInput) {
        throw new UnsupportedOperationException("Simple token cannot have partial parsing result");
    }

    /**
     * Since it's not expected to have any partial results or intermediate states, such token will always be completed.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isCompleted() {
        return true;
    }
}
