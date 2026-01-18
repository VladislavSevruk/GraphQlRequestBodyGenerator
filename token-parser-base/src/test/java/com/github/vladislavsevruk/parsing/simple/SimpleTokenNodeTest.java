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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleTokenNodeTest {
    @Test
    void emptyTokenNodeTest() {
        TokenNode tokenNode = SimpleTokenNode.empty();
        assertTrue(tokenNode.isEmpty());
        assertTrue(tokenNode.isCompleted());
        assertThrows(UnsupportedOperationException.class, () -> tokenNode.resumeParsing("ab", true));
    }

    @Test
    void nullTokenNodeTest() {
        TokenNode tokenNode = SimpleTokenNode.nullValue();
        assertFalse(tokenNode.isEmpty());
        assertTrue(tokenNode.isCompleted());
        assertThrows(UnsupportedOperationException.class, () -> tokenNode.resumeParsing("a", 1, false));
    }
}
