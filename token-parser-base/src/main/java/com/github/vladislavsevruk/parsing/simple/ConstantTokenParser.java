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
import com.github.vladislavsevruk.parsing.TokenParser;
import com.github.vladislavsevruk.parsing.TokenParsingResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Detects whether specific constant is present at received position of provided input. Parsing result will never have
 * intermediate state, meaning that associated {@link TokenNode} will only be created if desired token was fully
 * matched. If provided input is not sufficient to determine whether token is present, either additional input will be
 * requested using received parent node and start index or, if no such input expected, `mismatch` result will be
 * returned. Implementation is also using optional lookahead rule that can be used to determine that specific lexical
 * token finished, e.g. to differentiate desired 'false' token from invalid 'falsetto' token.
 *
 * @param <T> type of {@link CharSequence} implementation
 * @param <U> type of {@link TokenNode} to initialize
 */
public class ConstantTokenParser<T extends CharSequence, U extends TokenNode> implements TokenParser {
    private final T token;
    private final Function<T, U> tokenNodeInitializer;
    @Nullable
    private final Predicate<Character> lookaheadPredicate;

    private ConstantTokenParser(T token,
                                Function<T, U> tokenNodeInitializer,
                                @Nullable Predicate<Character> lookaheadPredicate)
    {
        this.token = token;
        this.tokenNodeInitializer = tokenNodeInitializer;
        this.lookaheadPredicate = lookaheadPredicate;
    }

    /**
     * Creates token parser for specific constant value detection without using lookahead symbol check.
     *
     * @param token expected constant to look for
     * @return new instance set for specific constant value detection
     */
    public static ConstantTokenParser<String, ConstantTokenNode> constant(String token) {
        return constant(token, null);
    }

    /**
     * Creates token parser for specific constant value detection. Received {@code lookaheadPredicate} is used to check
     * symbol following detected constant to false-positive matches. E.g. if specification allows only 'true' and
     * 'false' values, such lookahead will help to detect invalid 'falsetto' token.
     *
     * @param token              expected constant to look for
     * @param lookaheadPredicate special rule for verifying symbol following detected constant
     * @return new instance set for specific constant value detection
     */
    public static ConstantTokenParser<String, ConstantTokenNode> constant(String token,
                                                                          @Nullable Predicate<Character> lookaheadPredicate)
    {
        return constant(token, ConstantTokenNode::new, lookaheadPredicate);
    }

    /**
     * Creates token parser for specific constant value detection. Received {@code lookaheadPredicate} is used to check
     * symbol following detected constant to false-positive matches. E.g. if specification allows only 'true' and
     * 'false' values, such lookahead will help to detect invalid 'falsetto' token.
     *
     * @param token                expected constant to look for
     * @param tokenNodeInitializer initialization function used for specific {@link TokenNode} creation for matched
     *                             value
     * @param lookaheadPredicate   special rule for verifying symbol following detected constant
     * @param <T>                  type of {@link CharSequence} implementation
     * @param <U>                  type of {@link TokenNode} to initialize on match
     * @return new instance set for specific constant value detection
     */
    public static <T extends CharSequence, U extends TokenNode> ConstantTokenParser<T, U> constant(T token,
                                                                                                   Function<T, U> tokenNodeInitializer,
                                                                                                   @Nullable Predicate<Character> lookaheadPredicate)
    {
        return new ConstantTokenParser<>(token, tokenNodeInitializer, lookaheadPredicate);
    }

    /**
     * Parses received {@link CharSequence} input starting from the received index.
     *
     * @param content       input to process
     * @param startIndex    position (inclusive) of input to start parsing from
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @param parentNode    parent token node
     * @return parsing result of provided input from received start position
     * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than {@code length}
     */
    @Override
    public TokenParsingResult parse(CharSequence content,
                                    final int startIndex,
                                    final boolean hasMoreTokens,
                                    TokenNode parentNode)
    {
        int expectedRegionEnd = startIndex + token.length();
        boolean canCheckWholeToken = expectedRegionEnd <= content.length();
        if (!canCheckWholeToken && !hasMoreTokens) {
            return TokenParsingResult.mismatch();
        }
        int symbolsCount = canCheckWholeToken ? token.length() : content.length() - startIndex;
        for (int i = 0; i < symbolsCount; ++i) {
            if (content.charAt(startIndex + i) != token.charAt(i)) {
                return TokenParsingResult.mismatch();
            }
        }
        if (!canCheckWholeToken) {
            return TokenParsingResult.needMoreTokens(parentNode, startIndex);
        }
        if (lookaheadPredicate == null) {
            return match(expectedRegionEnd);
        }
        if (expectedRegionEnd == content.length()) {
            return hasMoreTokens ? TokenParsingResult.needMoreTokens(parentNode, startIndex) : match(expectedRegionEnd);
        }
        return lookaheadPredicate.test(content.charAt(expectedRegionEnd))
                ? match(expectedRegionEnd)
                : TokenParsingResult.mismatch();
    }

    private TokenParsingResult match(int expectedRegionEnd) {
        return TokenParsingResult.match(tokenNodeInitializer.apply(token), expectedRegionEnd);
    }

    /**
     * Token node with matched constant value.
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConstantTokenNode extends SimpleTokenNode {
        /**
         * -- GETTER --
         * Returns matched constant value
         *
         * @return constant value
         */
        @Getter
        private final String constant;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return constant;
        }
    }
}
