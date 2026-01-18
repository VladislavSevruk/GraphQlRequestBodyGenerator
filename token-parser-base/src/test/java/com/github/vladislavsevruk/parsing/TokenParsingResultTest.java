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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenParsingResultTest {
    @Test
    void matchedTokenParsingResultTest() {
        TokenNode tokenNode = mock(TokenNode.class);
        assertThrows(IllegalArgumentException.class, () -> TokenParsingResult.match(tokenNode, -1));
        when(tokenNode.isCompleted()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> TokenParsingResult.match(tokenNode, 3));
        when(tokenNode.isCompleted()).thenReturn(true);
        TokenParsingResult result = TokenParsingResult.match(tokenNode, 3);
        assertSame(tokenNode, result.getToken());
        assertEquals(3, result.getLastIndex());
        assertEquals("TokenParsingResult.match(3)", result.toString());
        assertTrue(result.isMatched());
        assertFalse(result.isMismatched());
        assertFalse(result.isMoreTokensNeeded());
    }

    @Test
    void needMoreTokensTokenParsingResultTest() {
        TokenNode tokenNode = mock(TokenNode.class);
        assertThrows(IllegalArgumentException.class, () -> TokenParsingResult.needMoreTokens(tokenNode, -1));
        when(tokenNode.isCompleted()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> TokenParsingResult.needMoreTokens(tokenNode, 3));
        when(tokenNode.isCompleted()).thenReturn(false);
        TokenParsingResult result = TokenParsingResult.needMoreTokens(tokenNode, 3);
        assertSame(tokenNode, result.getToken());
        assertEquals(3, result.getLastIndex());
        assertEquals("TokenParsingResult.needMoreTokens(3)", result.toString());
        assertTrue(result.isMoreTokensNeeded());
        assertFalse(result.isMatched());
        assertFalse(result.isMismatched());
        TokenNode tokenNode2 = mock(TokenNode.class);
        TokenParsingResult orElseResult = result.orElse(tokenNode2, 4);
        assertSame(result, orElseResult);
        assertSame(tokenNode, result.getToken());
        assertEquals(3, result.getLastIndex());
        assertEquals("TokenParsingResult.needMoreTokens(3)", result.toString());
        assertTrue(result.isMoreTokensNeeded());
        assertFalse(result.isMatched());
        assertFalse(result.isMismatched());
    }

    @Test
    void mismatchedTokenParsingResultTest() {
        TokenParsingResult result = TokenParsingResult.mismatch();
        assertThrows(IllegalStateException.class, result::getToken);
        assertEquals(-1, result.getLastIndex());
        assertEquals("TokenParsingResult.mismatch", result.toString());
        assertTrue(result.isMismatched());
        assertFalse(result.isMatched());
        assertFalse(result.isMoreTokensNeeded());
        TokenNode tokenNode = mock(TokenNode.class);
        assertThrows(IllegalArgumentException.class, () -> result.orElse(tokenNode, -1));
        when(tokenNode.isCompleted()).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> result.orElse(tokenNode, 3));
        when(tokenNode.isCompleted()).thenReturn(true);
        TokenParsingResult orElseResult = result.orElse(tokenNode, 4);
        assertSame(tokenNode, orElseResult.getToken());
        assertEquals(4, orElseResult.getLastIndex());
        assertEquals("TokenParsingResult.match(4)", orElseResult.toString());
        assertTrue(orElseResult.isMatched());
        assertFalse(orElseResult.isMismatched());
        assertFalse(orElseResult.isMoreTokensNeeded());
    }

    @Test
    void utilizedTokenParsingResultIsReusedTest() {
        TokenNode tokenNode1 = mock(TokenNode.class);
        when(tokenNode1.isCompleted()).thenReturn(true);
        TokenParsingResult result1 = TokenParsingResult.match(tokenNode1, 1);
        TokenParsingResult result2 = TokenParsingResult.mismatch();
        TokenNode tokenNode3 = mock(TokenNode.class);
        when(tokenNode3.isCompleted()).thenReturn(false);
        TokenParsingResult result3 = TokenParsingResult.needMoreTokens(tokenNode3, 3);
        assertNotSame(result1, result2);
        assertNotSame(result2, result3);
        assertNotSame(result1, result3);
        // cached instance will be reused
        result1.utilize();
        // mismatch is always reused
        TokenParsingResult result4 = TokenParsingResult.mismatch();
        TokenNode tokenNode5 = mock(TokenNode.class);
        when(tokenNode5.isCompleted()).thenReturn(false);
        TokenParsingResult result5 = TokenParsingResult.needMoreTokens(tokenNode5, 5);
        when(tokenNode3.isCompleted()).thenReturn(true);
        TokenParsingResult result6 = TokenParsingResult.match(tokenNode3, 6);
        assertSame(result1, result5);
        assertSame(result2, result4);
        assertNotSame(result3, result6);
        // cached instances will be reused in queue order
        result6.utilize();
        result5.utilize();
        TokenParsingResult result7 = TokenParsingResult.match(tokenNode3, 3);
        assertSame(result6, result7);
        // clearCaches removes cached results
        result7.utilize();
        TokenParsingResult.clearCaches();
        TokenParsingResult result8 = TokenParsingResult.match(tokenNode3, 3);
        assertNotSame(result5, result8);
        assertNotSame(result7, result8);
        // mismatch cannot be utilized
        result8.utilize();
        result4.utilize();
        TokenParsingResult result9 = TokenParsingResult.mismatch();
        assertNotSame(result4, result8);
        assertSame(result2, result9);
        assertSame(result4, result9);
    }
}
