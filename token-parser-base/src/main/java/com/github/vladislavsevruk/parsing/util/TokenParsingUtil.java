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
package com.github.vladislavsevruk.parsing.util;

import com.github.vladislavsevruk.parsing.TokenNode;
import com.github.vladislavsevruk.parsing.TokenParsingResult;
import com.github.vladislavsevruk.parsing.exception.ParsingInputException;
import org.jspecify.annotations.Nullable;

/**
 * Contains utility methods for parsing operations.
 */
public final class TokenParsingUtil {
    private TokenParsingUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * Identifies whether last index of input was reached there's no any other additional input that could've been
     * provided.
     *
     * @param content       current input to parse
     * @param index         current parsing position
     * @param hasMoreTokens identifies whether additional input can be provided by request
     * @return {@code true} if last index of current input is processed and there's no additional input that can be
     * provided, {@code false} otherwise
     */
    public static boolean isTheRealEndReached(CharSequence content, int index, boolean hasMoreTokens) {
        return index == content.length() && !hasMoreTokens;
    }

    /**
     * Checks if additional input can be provided and returns {@link TokenParsingResult} with such request. Throws
     * {@link TokenParsingResult} if received input part was the last one.
     *
     * @param content       current input to parse
     * @param index         current parsing position
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @param tokenNode     token node that requires additional input
     * @return token parsing result with request or additional input pointing to provided position
     * @throws ParsingInputException if additional input cannot be provided by request
     */
    public static TokenParsingResult needMoreTokens(CharSequence content,
                                                    int index,
                                                    boolean hasMoreTokens,
                                                    TokenNode tokenNode)
    {
        throwIfNoMoreTokens(content, hasMoreTokens);
        return TokenParsingResult.needMoreTokens(tokenNode, index);
    }

    /**
     * Checks if additional input can be provided and throws {@link TokenParsingResult} if received input part was the
     * last one.
     *
     * @param content       last provided input to parse
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @throws ParsingInputException if additional input cannot be provided by request
     */
    public static void throwIfNoMoreTokens(CharSequence content, boolean hasMoreTokens) {
        if (!hasMoreTokens) {
            throwNoMoreTokens(content, content.length());
        }
    }

    /**
     * Throws {@link ParsingInputException} with message describing expectations to get additional input and some area
     * of current input to highlight where exactly parsing was interrupted.
     *
     * @param content last provided input to parse
     * @param index   position where parsing was interrupted due to insufficient input
     * @throws ParsingInputException with message describing expectations to get additional input
     */
    public static void throwNoMoreTokens(CharSequence content, int index) {
        StringBuilder messageBuilder = new StringBuilder("Expected to receive more input, but the end was reached");
        messageBuilder = appendContentAroundIndex(messageBuilder, content, index);
        throwParsingInputException(messageBuilder.toString());
    }

    /**
     * Throws {@link ParsingInputException} with received message that describes the issue hit during parsing.
     *
     * @param message exception message describing issue
     * @throws ParsingInputException with message describing input parsing issue
     */
    public static void throwParsingInputException(String message) {
        throw new ParsingInputException(message);
    }

    /**
     * Generates message describing specification violation using received specification link (optional) and area of
     * current input where such violation was detected.
     *
     * @param content            last provided input to parse
     * @param index              position where parsing was interrupted due to specifications violation
     * @param specificationsLink optional link to specifications
     * @return generated message highlighting specification violation
     */
    public static String generateSpecificationsViolationMessage(CharSequence content,
                                                                int index,
                                                                @Nullable String specificationsLink)
    {
        if (index < 0 || index > content.length()) {
            throw new IllegalArgumentException("Index out of range: " + index + ", length: " + content.length());
        }
        StringBuilder messageBuilder = new StringBuilder("Input value violates specifications");
        if (specificationsLink != null && !specificationsLink.isEmpty()) {
            messageBuilder.append(' ').append(specificationsLink);
        }
        return appendContentAroundIndex(messageBuilder, content, index).toString();
    }

    private static StringBuilder appendContentAroundIndex(StringBuilder messageBuilder, CharSequence content, int index)
    {
        if (content.isEmpty()) {
            return messageBuilder;
        }
        int intend = 20;
        int leftIndex = Math.max(index - intend, 0);
        int rightIndex = Math.min(index + intend + 1, content.length());
        messageBuilder.append(", near '").append(content.subSequence(leftIndex, index)).append('`');
        if (index != content.length()) {
            messageBuilder.append(content.charAt(index));
        }
        messageBuilder.append('`');
        if (index < content.length() - 1) {
            messageBuilder.append(content.subSequence(index + 1, rightIndex));
        }
        return messageBuilder.append('\'');
    }
}
