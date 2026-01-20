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

import com.github.vladislavsevruk.parsing.TokenParser;
import com.github.vladislavsevruk.parsing.TokenParsingResult;

import static com.github.vladislavsevruk.parsing.simple.SimpleTokenNode.empty;

/**
 * Detects whether specific single symbol is present at received position of provided input.
 */
public class SingleSymbolTokenParser implements TokenParser {
    private final char token;

    private SingleSymbolTokenParser(char token) {
        this.token = token;
    }

    /**
     * Creates token parser for single symbol detection.
     *
     * @param token expected symbol to look for
     * @return new instance set for received symbol detection
     */
    public static SingleSymbolTokenParser symbol(char token) {
        return new SingleSymbolTokenParser(token);
    }

    /**
     * Detects whether specified symbol is present at received position of provided input. `match` result will contain
     * {@link SimpleTokenNode#empty} instance as there's no much use of returning specific predefined symbol. However,
     * if receiving such token is really necessary, using {@link ConstantTokenParser} may be preferred.
     *
     * @param content       input to process
     * @param startIndex    position (inclusive) of input to start parsing from
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @return result of symbol detection at provided input at received start position
     */
    @Override
    public TokenParsingResult parse(CharSequence content, final int startIndex, final boolean hasMoreTokens) {
        return content.charAt(startIndex) == token
                ? TokenParsingResult.match(empty(), startIndex + 1)
                : TokenParsingResult.mismatch();
    }
}
