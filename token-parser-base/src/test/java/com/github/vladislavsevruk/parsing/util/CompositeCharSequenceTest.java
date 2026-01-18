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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompositeCharSequenceTest {
    @Test
    void compositeCharSequenceTest() {
        char[] input = new char[] { 'a', 'b', 'c', 'd' };
        CompositeCharSequence compositeCharSequence = new CompositeCharSequence(input, 3);
        assertEquals(3, compositeCharSequence.length());
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(3));
        assertEquals('c', compositeCharSequence.charAt(2));
        assertEquals("", compositeCharSequence.subSequence(3, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(3, 4));
        assertEquals("bc", compositeCharSequence.subSequence(1, 3));
        assertEquals("ab", compositeCharSequence.subSequence(0, 2));
        assertEquals("abc", compositeCharSequence.subSequence(0, 3));
        assertEquals("abc", compositeCharSequence.toString());
        compositeCharSequence.preSaveLeftover(1);
        assertEquals(2, compositeCharSequence.length());
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(2));
        assertEquals('c', compositeCharSequence.charAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 3));
        assertEquals("bc", compositeCharSequence.subSequence(0, 2));
        assertEquals("b", compositeCharSequence.subSequence(0, 1));
        assertEquals("c", compositeCharSequence.subSequence(1, 2));
        assertEquals("bc", compositeCharSequence.toString());
    }

    @Test
    void compositeCharSequencePart2Test() {
        char[] input = new char[] { 'a', 'b', 'c', 'd' };
        CompositeCharSequence compositeCharSequence = new CompositeCharSequence(input, 3);
        compositeCharSequence.preSaveLeftover(1);
        compositeCharSequence.preSaveLeftover(0);
        assertEquals(2, compositeCharSequence.length());
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(2));
        assertEquals('c', compositeCharSequence.charAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 3));
        assertEquals("bc", compositeCharSequence.subSequence(0, 2));
        assertEquals("b", compositeCharSequence.subSequence(0, 1));
        assertEquals("c", compositeCharSequence.subSequence(1, 2));
        assertEquals("bc", compositeCharSequence.toString());
        compositeCharSequence.setCurrent(input);
        assertEquals(6, compositeCharSequence.length());
        assertEquals("bcabcd", compositeCharSequence.subSequence(0, 6));
        assertEquals("ca", compositeCharSequence.subSequence(1, 3));
        assertEquals("bcabcd", compositeCharSequence.toString());
        compositeCharSequence.preSaveLeftover(0);
        compositeCharSequence.setCurrent(input, 2);
        assertEquals(8, compositeCharSequence.length());
        assertEquals("bcabcdab", compositeCharSequence.subSequence(0, 8));
        assertEquals("cabcda", compositeCharSequence.subSequence(1, 7));
        assertEquals("bcabcdab", compositeCharSequence.toString());
    }

    @Test
    void compositeCharSequencePart3Test() {
        char[] input = new char[] { 'a', 'b', 'c', 'd' };
        CompositeCharSequence compositeCharSequence = new CompositeCharSequence(input, 3);
        compositeCharSequence.preSaveLeftover(1);
        compositeCharSequence.preSaveLeftover(0);
        compositeCharSequence.setCurrent(input);
        compositeCharSequence.preSaveLeftover(0);
        compositeCharSequence.setCurrent(input, 2);
        compositeCharSequence.preSaveLeftover(2);
        assertEquals(6, compositeCharSequence.length());
        assertEquals("abcdab", compositeCharSequence.subSequence(0, 6));
        assertEquals("da", compositeCharSequence.subSequence(3, 5));
        assertEquals("abcdab", compositeCharSequence.toString());
        compositeCharSequence.setCurrent(input);
        assertEquals(10, compositeCharSequence.length());
        compositeCharSequence.setCurrent(input);
        assertEquals(4, compositeCharSequence.length());
        assertEquals("abcd", compositeCharSequence.subSequence(0, 4));
        assertEquals("cd", compositeCharSequence.subSequence(2, 4));
        assertEquals("abcd", compositeCharSequence.toString());
        compositeCharSequence.preSaveLeftover(4);
        assertEquals(0, compositeCharSequence.length());
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 1));
        assertEquals("", compositeCharSequence.toString());
    }

    @Test
    void compositeCharSequenceEmptyArrayAtTheStartTest() {
        char[] emptyArray = new char[0];
        CompositeCharSequence emptyCompositeCharSequence = new CompositeCharSequence(emptyArray);
        assertEquals(0, emptyCompositeCharSequence.length());
        char[] input = new char[] { 'a', 'b', 'c', 'd' };
        emptyCompositeCharSequence.setCurrent(input);
        assertEquals(input.length, emptyCompositeCharSequence.length());
        assertEquals('c', emptyCompositeCharSequence.charAt(2));
        emptyCompositeCharSequence.setCurrent(emptyArray);
        assertEquals(0, emptyCompositeCharSequence.length());
        emptyCompositeCharSequence.preSaveLeftover(0);
        assertEquals(0, emptyCompositeCharSequence.length());
        emptyCompositeCharSequence.setCurrent(input);
        assertEquals(input.length, emptyCompositeCharSequence.length());
        assertEquals('c', emptyCompositeCharSequence.charAt(2));
        emptyCompositeCharSequence.preSaveLeftover(1);
        assertEquals(3, emptyCompositeCharSequence.length());
        assertEquals('d', emptyCompositeCharSequence.charAt(2));
    }

    @Test
    void compositeCharSequenceInvalidInputTest() {
        char[] emptyArray = new char[0];
        assertThrows(IndexOutOfBoundsException.class, () -> new CompositeCharSequence(emptyArray, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> new CompositeCharSequence(emptyArray, 1));
        CompositeCharSequence emptyCompositeCharSequence = new CompositeCharSequence(emptyArray);
        assertThrows(IndexOutOfBoundsException.class, () -> emptyCompositeCharSequence.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> emptyCompositeCharSequence.charAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> emptyCompositeCharSequence.subSequence(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> emptyCompositeCharSequence.subSequence(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> emptyCompositeCharSequence.subSequence(0, 1));
        char[] input = new char[] { 'a', 'b', 'c', 'd' };
        assertThrows(IndexOutOfBoundsException.class, () -> new CompositeCharSequence(input, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> new CompositeCharSequence(input, 5));
        CompositeCharSequence compositeCharSequence = new CompositeCharSequence(input);
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(4));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.preSaveLeftover(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.preSaveLeftover(5));
        compositeCharSequence.preSaveLeftover(1);
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 4));
        compositeCharSequence.preSaveLeftover(1);
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.charAt(2));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.subSequence(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.setCurrent(input, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> compositeCharSequence.setCurrent(input, 5));
        compositeCharSequence.setCurrent(input, 3);
    }
}
