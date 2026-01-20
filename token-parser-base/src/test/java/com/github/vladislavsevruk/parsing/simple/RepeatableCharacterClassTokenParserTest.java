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

import java.util.function.Predicate;

import static com.github.vladislavsevruk.parsing.LazyTokenNodeInitializer.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepeatableCharacterClassTokenParserTest {
    @Test
    void parseMatchingOncePredicateNoLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate);
        TokenNode tokenNode = mock(TokenNode.class);
        TokenParsingResult result = parser.parse("abc", 0, false);
        assertTrue(result.isMatched());
        assertEquals(1, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 0, true);
        assertTrue(result.isMatched());
        assertEquals(1, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, false);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, true);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, false);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMatchingTwicePredicateNoLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate);
        TokenNode tokenNode = mock(TokenNode.class);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 0, true);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, false);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, true);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, false);
        assertTrue(result.isMatched());
        assertEquals(4, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMismatchingPredicateNoLookaheadTest() {
        Predicate<Character> predicate = character -> false;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 0, true);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, true);
        assertTrue(result.isMismatched());
    }

    @Test
    void parseMatchingOncePredicateMatchingLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        Predicate<Character> lookaheadPredicate = character -> true;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abc", 0, false);
        assertTrue(result.isMatched());
        assertEquals(1, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 0, true);
        assertTrue(result.isMatched());
        assertEquals(1, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, false);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, true);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, false);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMatchingTwicePredicateMatchingLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        Predicate<Character> lookaheadPredicate = character -> true;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 0, true);
        assertTrue(result.isMatched());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, false);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, true);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, false);
        assertTrue(result.isMatched());
        assertEquals(4, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMismatchingPredicateMatchingLookaheadTest() {
        Predicate<Character> predicate = character -> false;
        Predicate<Character> lookaheadPredicate = character -> true;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 0, true);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, true);
        assertTrue(result.isMismatched());
    }

    @Test
    void parseMatchingOncePredicateMismatchingLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        Predicate<Character> lookaheadPredicate = character -> false;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abc", 0, false);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 0, true);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, false);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 1, true);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, false);
        assertTrue(result.isMatched());
        assertEquals(3, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(false);
        result = parser.parse("abc", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMatchingTwicePredicateMismatchingLookaheadTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        Predicate<Character> lookaheadPredicate = character -> false;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 0, true);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, false);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 1, true);
        assertTrue(result.isMismatched());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, false);
        assertTrue(result.isMatched());
        assertEquals(4, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        result = parser.parse("abcd", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(2, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }

    @Test
    void parseMismatchingPredicateMismatchingLookaheadTest() {
        Predicate<Character> predicate = character -> false;
        Predicate<Character> lookaheadPredicate = character -> false;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, lookaheadPredicate);
        TokenParsingResult result = parser.parse("abcd", 0, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 0, true);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, false);
        assertTrue(result.isMismatched());
        result = parser.parse("ab", 1, true);
        assertTrue(result.isMismatched());
    }

    @Test
    void parseNotSignificantValueTest() {
        Predicate<Character> predicate = mock(Predicate.class);
        when(predicate.test(any())).thenReturn(true).thenReturn(true).thenReturn(false);
        Predicate<Character> lookaheadPredicate = character -> false;
        RepeatableCharacterClassTokenParser<TokenNode> parser
                = RepeatableCharacterClassTokenParser.repeatableCharacterClass(predicate, empty(), lookaheadPredicate, false);
        TokenParsingResult result = parser.parse("abcd", 2, true);
        assertTrue(result.isMoreTokensNeeded());
        assertEquals(4, result.getLastIndex());
        assertTrue(result.getToken().isEmpty());
    }
}
