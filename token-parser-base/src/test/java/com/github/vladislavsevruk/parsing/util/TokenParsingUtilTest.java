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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenParsingUtilTest {
    @Test
    void isTheRealEndReachedTest() {
        assertFalse(TokenParsingUtil.isTheRealEndReached("ab", 1, true));
        assertFalse(TokenParsingUtil.isTheRealEndReached("ab", 1, false));
        assertFalse(TokenParsingUtil.isTheRealEndReached("ab", 2, true));
        assertTrue(TokenParsingUtil.isTheRealEndReached("ab", 2, false));
    }

    @Test
    void needMoreTokensTest() {
        ParsingInputException exception = assertThrows(ParsingInputException.class,
                () -> TokenParsingUtil.needMoreTokens("ab", 1, false, mock(TokenNode.class)));
        assertTrue(exception.getMessage().endsWith(", near 'ab``'"));
        TokenNode tokenNode = mock(TokenNode.class);
        when(tokenNode.isCompleted()).thenReturn(false);
        TokenParsingResult result = TokenParsingUtil.needMoreTokens("ab", 1, true, tokenNode);
        assertTrue(result.isMoreTokensNeeded());
        assertSame(tokenNode, result.getToken());
        assertEquals(1, result.getLastIndex());
    }

    @Test
    void throwIfNoMoreTokensTest() {
        assertThrows(ParsingInputException.class, () -> TokenParsingUtil.throwIfNoMoreTokens("ab", false));
        assertDoesNotThrow(() -> TokenParsingUtil.throwIfNoMoreTokens("ab", true));
    }

    @Test
    void generateSpecificationsViolationMessageTest() {
        assertEquals("Input value violates specifications",
                TokenParsingUtil.generateSpecificationsViolationMessage("", 0, null));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", -1, null));
        assertEquals("Input value violates specifications, near '`a`b'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 0, null));
        assertEquals("Input value violates specifications, near 'a`b`'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 1, null));
        assertEquals("Input value violates specifications, near 'ab``'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 2, null));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", 3, null));
        assertEquals("Input value violates specifications",
                TokenParsingUtil.generateSpecificationsViolationMessage("", 0, ""));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", -1, ""));
        assertEquals("Input value violates specifications, near '`a`b'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 0, ""));
        assertEquals("Input value violates specifications, near 'a`b`'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 1, ""));
        assertEquals("Input value violates specifications, near 'ab``'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 2, ""));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", 3, ""));
        String specificationsLink = "cdef";
        assertEquals("Input value violates specifications cdef",
                TokenParsingUtil.generateSpecificationsViolationMessage("", 0, specificationsLink));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", -1, specificationsLink));
        assertEquals("Input value violates specifications cdef, near '`a`b'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 0, specificationsLink));
        assertEquals("Input value violates specifications cdef, near 'a`b`'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 1, specificationsLink));
        assertEquals("Input value violates specifications cdef, near 'ab``'",
                TokenParsingUtil.generateSpecificationsViolationMessage("ab", 2, specificationsLink));
        assertThrows(IllegalArgumentException.class,
                () -> TokenParsingUtil.generateSpecificationsViolationMessage("ab", 3, specificationsLink));
    }

    @Test
    void attemptToInitializeTest() {
        Constructor<?> constructor = TokenParsingUtil.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }
}
