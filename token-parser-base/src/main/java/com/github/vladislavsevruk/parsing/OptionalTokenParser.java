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
 * Implementation of {@link TokenParser} that specifies optional appearance of some token. This basically means
 * returned {@link TokenParsingResult} will never have `mismatch` value, `match` value with received startIndex
 * will be returned instead. For other cases (`match` or `needMoreTokens`) token will be processed as usual.
 */
public final class OptionalTokenParser implements TokenParser {
    private final TokenParser delegate;

    private OptionalTokenParser(TokenParser delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates optional appearance wrapper for received {@link TokenParser}. Such instance will never return
     * `mismatch` value.
     *
     * @param delegate token parser that will be used for token parsing attempt
     * @return created optional token parser that will parse token if it appears or will point on previous position
     * otherwise
     */
    public static OptionalTokenParser optional(TokenParser delegate) {
        return new OptionalTokenParser(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenParsingResult parse(CharSequence content, int startIndex, boolean hasMoreTokens, TokenNode parentNode) {
        return delegate.parse(content, startIndex, hasMoreTokens, parentNode).orElse(empty(), startIndex);
    }
}
