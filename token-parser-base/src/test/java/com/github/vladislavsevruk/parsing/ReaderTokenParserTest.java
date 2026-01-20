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
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReaderTokenParserTest {
    @Test
    void parseMatchIteratedTest() throws IOException {
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.resumeParsing(any(), eq(false))).then(matchAnswer("abc", tokenNode, 3));
        when(tokenNode.isCompleted()).thenReturn(false).thenReturn(true);
        TokenParser tokenParser = mock(TokenParser.class);
        when(tokenParser.parse(any(), eq(true))).then(needMoreTokensAnswer("ab", tokenNode, 0));
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser, 2);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).then(setCharsAnswer('a', 'b')).then(setCharsAnswer('c'));
        TokenNode result = readerTokenParser.parse(reader);
        assertSame(tokenNode, result);
        assertTrue(result.isCompleted());
    }

    @Test
    void parseMatchWhileInputAtOnceTest() throws IOException {
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.isCompleted()).thenReturn(true);
        TokenParser tokenParser = mock(TokenParser.class);
        when(tokenParser.parse(any(), eq(false))).then(matchAnswer("abcd", tokenNode, 0));
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).then(setCharsAnswer('a', 'b', 'c', 'd'));
        TokenNode result = readerTokenParser.parse(reader);
        assertSame(tokenNode, result);
        assertTrue(result.isCompleted());
    }

    @Test
    void parseMatchWithFullBufferLastCallTest() throws IOException {
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.resumeParsing(any(), eq(true))).then(matchAnswer("abcd", tokenNode, 4));
        when(tokenNode.isCompleted()).thenReturn(false).thenReturn(true);
        TokenParser tokenParser = mock(TokenParser.class);
        when(tokenParser.parse(any(), eq(true))).then(needMoreTokensAnswer("ab", tokenNode, 0));
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser, 2);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).then(setCharsAnswer('a', 'b')).then(setCharsAnswer('c', 'd'));
        TokenNode result = readerTokenParser.parse(reader);
        assertSame(tokenNode, result);
        assertTrue(result.isCompleted());
    }

    @Test
    void parseExtraCallAtTheEndTest() throws IOException {
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.resumeParsing(any(), eq(true))).then(needMoreTokensAnswer("abcd", tokenNode, 3));
        when(tokenNode.resumeParsing(any(), eq(false))).then(matchAnswer("d", tokenNode, 4));
        when(tokenNode.isCompleted()).thenReturn(false).thenReturn(false).thenReturn(true);
        TokenParser tokenParser = mock(TokenParser.class);
        when(tokenParser.parse(any(), eq(true))).then(needMoreTokensAnswer("ab", tokenNode, 0));
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser, 2);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).then(setCharsAnswer('a', 'b')).then(setCharsAnswer('c', 'd'))
                .thenReturn(-1);
        TokenNode result = readerTokenParser.parse(reader);
        assertSame(tokenNode, result);
        assertTrue(result.isCompleted());
    }

    @Test
    void parseEmptyTest() throws IOException {
        TokenParser tokenParser = mock(TokenParser.class);
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser, 2);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).thenReturn(-1);
        TokenNode result = readerTokenParser.parse(reader);
        assertTrue(result.isEmpty());
        assertTrue(result.isCompleted());
    }

    @Test
    void parseMismatchTest() throws IOException {
        TokenParser tokenParser = mock(TokenParser.class);
        when(tokenParser.parse(any(), eq(false))).then(mismatchAnswer("a"));
        ReaderTokenParser readerTokenParser = new ReaderTokenParser(tokenParser, 2);
        Reader reader = mock(Reader.class);
        when(reader.read(any(char[].class))).then(setCharsAnswer('a'));
        assertThrows(ParsingInputException.class, () -> readerTokenParser.parse(reader));
    }

    private Answer<?> matchAnswer(String expected, TokenNode tokenNode, int index) {
        return invocation -> {
            assertEquals(expected, invocation.getArgument(0, CharSequence.class).toString());
            return TokenParsingResult.match(tokenNode, index);
        };
    }

    private Answer<?> mismatchAnswer(String expected) {
        return invocation -> {
            assertEquals(expected, invocation.getArgument(0, CharSequence.class).toString());
            return TokenParsingResult.mismatch();
        };
    }

    private Answer<?> needMoreTokensAnswer(String expected, TokenNode tokenNode, int index) {
        return invocation -> {
            assertEquals(expected, invocation.getArgument(0, CharSequence.class).toString());
            return TokenParsingResult.needMoreTokens(tokenNode, index);
        };
    }

    private Answer<?> setCharsAnswer(char... chars) {
        if (chars.length == 0) {
            throw new IllegalArgumentException();
        }
        return invocation -> {
            char[] arg = invocation.getArgument(0, char[].class);
            System.arraycopy(chars, 0, arg, 0, chars.length);
            return chars.length;
        };
    }
}
