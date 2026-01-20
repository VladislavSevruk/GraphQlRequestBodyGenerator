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

/**
 * Implementation of {@link TokenParser} that specifies appearance of some token among provided variants. This basically
 * means that first found {@link TokenParsingResult} with non-`mismatch` value be returned, so order of delegate
 * {@link TokenParser} array makes a difference. If none of provided delegates `mismatch` result will be returned.
 */
public class AnyOfTokenParser implements TokenParser {
    private final TokenParser[] delegates;

    private AnyOfTokenParser(TokenParser[] delegates) {
        this.delegates = delegates;
    }

    /**
     * Creates wrapper for received {@link TokenParser} instances that delegates calls to underlying instances and
     * returns first found non `mismatch` result. If none of delegates returns such result, `mismatch` result is
     * returned.
     *
     * @param delegates token parsers that will be used for tokens parsing attempt
     * @return created wrapper for token parsers that return first non-failed parsing result if any
     */
    public static AnyOfTokenParser anyOf(TokenParser... delegates) {
        if (delegates.length < 2) {
            throw new IllegalArgumentException("AnyOfTokenParser requires at least two parsers");
        }
        TokenParser[] combinedParsers = new TokenParser[delegates.length];
        System.arraycopy(delegates, 0, combinedParsers, 0, delegates.length);
        return new AnyOfTokenParser(combinedParsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenParsingResult parse(CharSequence content, final int startIndex, final boolean hasMoreTokens) {
        for (TokenParser delegate : delegates) {
            TokenParsingResult lastResult = delegate.parse(content, startIndex, hasMoreTokens);
            if (!lastResult.isMismatched()) {
                return lastResult;
            }
        }
        return TokenParsingResult.mismatch();
    }
}
