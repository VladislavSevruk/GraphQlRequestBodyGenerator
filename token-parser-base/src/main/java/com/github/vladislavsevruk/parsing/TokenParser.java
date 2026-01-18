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

import com.github.vladislavsevruk.parsing.exception.ParsingInputException;

/**
 * Parses received input into associated {@link TokenNode} according to designed specifications and rules.
 * In general, implementations are following next assumptions for simplicity and consistency:
 * <ul>
 *     <li>start index is pointing exactly at position of {@link CharSequence} where start of the token is
 *     expected;</li>
 *     <li>if {@code hasMoreTokens} is {@code false}, then `needMoreTokens` result will never be returned;</li>
 *     <li>if {@link #isOptional} returns {@code true}, then `mismatch` result will never be returned. By default,
 *     `match` result with received {@code parentNode} that points
 *     to received {@code startIndex} will be used instead;</li>
 *     <li>if provided input is sufficient to determine that token is present, then:
 *     <ul>
 *         <li>if token can be parsed straight away, then returned result will be `match` with {@link TokenNode}
 *         containing parsed value and pointing to the position right after the latest token index;</li>
 *         <li>if input breaks before the expected token end, then the outcome depends on {@code hasMoreTokens}
 *         value:
 *         <ul>
 *             <li>If flag value is {@code true}, then `needMoreTokens` result will be returned with index pointing to
 *             the start position of last non-parsed entity. Result will also contain partially parsed {@link TokenNode}
 *             if it can have intermediate state or {@code parentNode} otherwise;</li>
 *             <li>If flag value is {@code false}, then {@link ParsingInputException} with violation description
 *             will be thrown.</li>
 *         </ul>
 *         </li>
 *     </ul>
 *     </li>
 *     <li>if provided input is sufficient to determine that token is not present, then `mismatch` value will be
 *     returned;</li>
 *     <li>if provided input is not sufficient to determine whether token is present or not, then the outcome
 *     depends on {@code hasMoreTokens} value:
 *     <ul>
 *         <li>If flag value is {@code true}, then `needMoreTokens` result will be returned with received
 *         {@code parentNode} that points to received {@code startIndex};</li>
 *         <li>If flag value is {@code false}, then `mismatch` value will be returned.
 *         </li>
 *     </ul>
 *     </li>
 * </ul>
 */
@FunctionalInterface
public interface TokenParser {
    /**
     * Parses received {@link CharSequence} input starting from the received index. Please refer to the class
     * description for general assumptions for implementations.
     *
     * @param content       input to process
     * @param startIndex    position (inclusive) of input to start parsing from
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @param parentNode    parent token node or root node if parsing document root
     * @return parsing result of provided input from received start position
     * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than {@code length}
     * @throws ParsingInputException     if provided input is sufficient to determine that desired token is present but
     *                                   structure of that token violates specified rules of expected token structure
     */
    TokenParsingResult parse(CharSequence content,
                             final int startIndex,
                             final boolean hasMoreTokens,
                             TokenNode parentNode) throws ParsingInputException;

    /**
     * Parses received {@link CharSequence} input as root token parser starting from the very beginning without
     * providing any additional input by request.
     *
     * @param content input to process
     * @return parsing result of provided input from the very beginning
     */
    default TokenParsingResult parse(CharSequence content) {
        return parse(content, false);
    }

    /**
     * Parses received {@link CharSequence} input as root token parser starting from the very beginning. Please refer
     * to the class description for general assumptions for implementations.
     *
     * @param content       input to process
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @return parsing result of provided input from the very beginning
     */
    default TokenParsingResult parse(CharSequence content, final boolean hasMoreTokens) {
        return parse(content, 0, hasMoreTokens);
    }

    /**
     * Parses received {@link CharSequence} input as root token parser starting from the received index. Please refer to
     * the class description for general assumptions for implementations.
     *
     * @param content       input to process
     * @param startIndex    position (inclusive) of input to start parsing from
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @return parsing result of provided input from received start position
     */
    default TokenParsingResult parse(CharSequence content, final int startIndex, final boolean hasMoreTokens) {
        throw new UnsupportedOperationException("Should be overridden in implementation if intended to use");
    }

    /**
     * Parses received {@link CharSequence} input starting from the very beginning. Please refer to
     * the class description for general assumptions for implementations.
     *
     * @param content       input to process
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @param parentNode    parent token node or root node if parsing document root
     * @return parsing result of provided input from the very beginning
     */
    default TokenParsingResult parse(CharSequence content, final boolean hasMoreTokens, TokenNode parentNode) {
        return parse(content, 0, hasMoreTokens, parentNode);
    }

    /**
     * Returns optional appearance wrapper for current {@link TokenParser} instance. Such instance will never return
     * `mismatch` value.
     *
     * @return optional token parser that will parse token if it appears or will point on previous position otherwise
     */
    default TokenParser optional() {
        return OptionalTokenParser.optional(this);
    }

    /**
     * Identifies whether current instance supports optional appearance of some token. Such instance will never return
     * `mismatch` value.
     *
     * @return {@code true} if current instance supports optional appearance of some token, {@code false} otherwise
     */
    default boolean isOptional() {
        return this instanceof OptionalTokenParser;
    }
}
