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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnyOfTokenParserTest {
    @Test
    void anyOfParserNoDelegatesTest() {
        assertThrows(IllegalArgumentException.class, AnyOfTokenParser::anyOf);
    }

    @Test
    void anyOfParserOnlyOneDelegateTest() {
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        assertThrows(IllegalArgumentException.class, () -> AnyOfTokenParser.anyOf(delegate1));
    }

    @Test
    void anyOfParserTokenBothDelegatesMismatchTest() {
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("ab", 1, false);
        assertTrue(result.isMismatched());
    }

    @Test
    void anyOfParserTokenBothDelegatesMatchTest() {
        TokenNode tokenNode1 = mock(TokenNode.class);
        when(tokenNode1.isCompleted()).thenReturn(true);
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.match(tokenNode1, 2);
        TokenNode tokenNode2 = mock(TokenNode.class);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.match(tokenNode2, 3);
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("abcd", 1, false);
        assertTrue(result.isMatched());
        assertEquals(tokenNode1, result.getToken());
        assertEquals(2, result.getLastIndex());
    }

    @Test
    void anyOfParserTokenBothDelegatesNeedMoreInputTest() {
        TokenNode tokenNode1 = mock(TokenNode.class);
        when(tokenNode1.isCompleted()).thenReturn(false);
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.needMoreTokens(tokenNode1, 2);
        TokenNode tokenNode2 = mock(TokenNode.class);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.needMoreTokens(tokenNode2, 3);
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("abcd", 1, false);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(tokenNode1, result.getToken());
        assertEquals(2, result.getLastIndex());
    }

    @Test
    void anyOfParserTokenFirstDelegateMatchTest() {
        TokenNode tokenNode1 = mock(TokenNode.class);
        when(tokenNode1.isCompleted()).thenReturn(true);
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.match(tokenNode1, 2);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("ab", 1, false);
        assertTrue(result.isMatched());
        assertEquals(tokenNode1, result.getToken());
        assertEquals(2, result.getLastIndex());
    }

    @Test
    void anyOfParserTokenFirstDelegateNeedMoreInputTest() {
        TokenNode tokenNode1 = mock(TokenNode.class);
        when(tokenNode1.isCompleted()).thenReturn(false);
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.needMoreTokens(tokenNode1, 2);
        TokenNode tokenNode2 = mock(TokenNode.class);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.match(tokenNode2, 3);
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("ab", 1, false);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(tokenNode1, result.getToken());
        assertEquals(2, result.getLastIndex());
    }

    @Test
    void anyOfParserTokenSecondDelegateMatchTest() {
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        TokenNode tokenNode2 = mock(TokenNode.class);
        when(tokenNode2.isCompleted()).thenReturn(true);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.match(tokenNode2, 3);
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("ab", 1, false);
        assertTrue(result.isMatched());
        assertEquals(tokenNode2, result.getToken());
        assertEquals(3, result.getLastIndex());
    }

    @Test
    void anyOfParserTokenSecondDelegateNeedMoreInputTest() {
        TokenParser delegate1 = (content, index, hasMoreInput) -> TokenParsingResult.mismatch();
        TokenNode tokenNode2 = mock(TokenNode.class);
        when(tokenNode2.isCompleted()).thenReturn(false);
        TokenParser delegate2 = (content, index, hasMoreInput) -> TokenParsingResult.needMoreTokens(tokenNode2, 3);
        AnyOfTokenParser anyOfTokenParser = AnyOfTokenParser.anyOf(delegate1, delegate2);
        TokenParsingResult result = anyOfTokenParser.parse("ab", 1, false);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(tokenNode2, result.getToken());
        assertEquals(3, result.getLastIndex());
    }
}
