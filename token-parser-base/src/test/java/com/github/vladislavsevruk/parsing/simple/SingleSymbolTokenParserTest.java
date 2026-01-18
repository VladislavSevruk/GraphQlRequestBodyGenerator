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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SingleSymbolTokenParserTest {
    @Test
    void parseTest() {
        TokenNode tokenNode = mock(TokenNode.class);
        SingleSymbolTokenParser parser = SingleSymbolTokenParser.symbol('b');
        TokenParsingResult result = parser.parse("ab", false, tokenNode);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", true, tokenNode);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, true, tokenNode);
        assertTrue(result.isMatched());
        assertTrue(result.getToken().isEmpty());
        assertEquals(2, result.getLastIndex());
        result = parser.parse("ab", 1, true, tokenNode);
        assertTrue(result.isMatched());
        assertTrue(result.getToken().isEmpty());
        assertEquals(2, result.getLastIndex());
    }
}
