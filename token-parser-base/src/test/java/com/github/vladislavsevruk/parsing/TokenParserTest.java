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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenParserTest {
    @Test
    void optionalParserTest() {
        TokenParser tokenParser = (content, index, hasMoreInput, node) -> TokenParsingResult.mismatch();
        assertFalse(tokenParser.isOptional());
        TokenParser optionalTokenParser = tokenParser.optional();
        assertTrue(optionalTokenParser.isOptional());
    }

    @Test
    void notOverriddenDefaultMethodsTest() {
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.isCompleted()).thenReturn(true);
        TokenParser tokenParser = (content, index, hasMoreInput, node) -> TokenParsingResult.match(node, index);
        assertThrows(UnsupportedOperationException.class, () -> tokenParser.parse("ab"));
        assertThrows(UnsupportedOperationException.class, () -> tokenParser.parse("ab", true));
        assertThrows(UnsupportedOperationException.class, () -> tokenParser.parse("ab", 1, false));
        TokenParsingResult result = tokenParser.parse("ab", true, tokenNode);
        assertTrue(result.isMatched());
        assertEquals(0, result.getLastIndex());
        assertSame(tokenNode, result.getToken());
    }
}
