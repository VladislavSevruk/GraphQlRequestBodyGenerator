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

import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

/**
 * This {@link CharSequence} implementation acts as wrapper for char array with functionality designed to simplify
 * continuous processing of text input read in chunks. This class doesn't create copy of arrays provided via constructor
 * and {@link #setCurrent} methods and acts as a wrapper for these arrays. This effectively means that any external
 * modification of these arrays will make impact on content of this class. Meanwhile {@link #preSaveLeftover} will
 * actually create copy of array region to save its state. This class is not thread-safe.
 */
@NotThreadSafe
public class CompositeCharSequence implements CharSequence {
    private char @Nullable [] leftover;
    private char @Nullable [] current;
    private int leftoverLength = 0;
    private int length;
    @Nullable
    private String cachedString;

    /**
     * Creates new instance with initial char array as input.
     *
     * @param chars initial char array
     */
    public CompositeCharSequence(char[] chars) {
        this(chars, chars.length);
    }

    /**
     * Creates new instance with initial char array and specified length of useful input.
     *
     * @param chars  initial char array
     * @param length length of working area withing input array
     * @throws IndexOutOfBoundsException if provided length is negative or greater than array length
     */
    public CompositeCharSequence(char[] chars, int length) {
        if (length < 0 || chars.length < length) {
            throw new IndexOutOfBoundsException("Array length" + chars.length + ", Length: " + length);
        }
        this.current = chars;
        this.length = length;
    }

    /**
     * Pre-saves current input from received index as leftover before next input part. Such pre-saved part will be used
     * as prefix for next input received by following {@link #setCurrent} call while all input before index will be
     * removed. Calling mentioned methods without pre-saving leftover will simply replace whole working input.
     *
     * @param start index (inclusive) of working area to keep
     * @throws IndexOutOfBoundsException if provided index is negative or greater than current length
     */
    public void preSaveLeftover(int start) {
        if (start < 0 || start > length()) {
            throw new IndexOutOfBoundsException("Index: " + start + ", Length: " + length());
        }
        if (start == 0 && length == 0) {
            return;
        }
        if (start < leftoverLength) {
            int newLeftoverLength = leftoverLength - start;
            char[] newLeftover = new char[newLeftoverLength + length];
            //noinspection DataFlowIssue
            System.arraycopy(leftover, start, newLeftover, 0, newLeftoverLength);
            if (length != 0) {
                //noinspection DataFlowIssue
                System.arraycopy(current, 0, newLeftover, newLeftoverLength, length);
            }
            leftover = newLeftover;
            leftoverLength = newLeftover.length;
        } else {
            int subIndex = start - leftoverLength;
            if (subIndex == length) {
                leftover = null;
                leftoverLength = 0;
            } else {
                char[] newLeftover = new char[length - subIndex];
                //noinspection DataFlowIssue
                System.arraycopy(current, subIndex, newLeftover, 0, newLeftover.length);
                leftover = newLeftover;
                leftoverLength = newLeftover.length;
            }
        }
        current = null;
        length = 0;
        cachedString = null;
    }

    /**
     * Sets received char array as current working input. Calling without preceding {@link #preSaveLeftover} invocation
     * will replace the whole content.
     *
     * @param next new current working char array
     */
    public void setCurrent(char[] next) {
        setCurrent(next, next.length);
    }

    /**
     * Sets received char array as current working input with specified useful length. Calling without preceding
     * {@link #preSaveLeftover} invocation will replace the whole content.
     *
     * @param next       new current working char array
     * @param nextLength length of working area withing input array
     * @throws IndexOutOfBoundsException if provided length is negative or greater than array length
     */
    public void setCurrent(char[] next, int nextLength) {
        if (nextLength < 0 || next.length < nextLength) {
            throw new IndexOutOfBoundsException("Array length" + next.length + ", Length: " + length);
        }
        if (length != 0) {
            leftover = null;
            leftoverLength = 0;
        }
        current = next;
        length = nextLength;
        cachedString = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int length() {
        return leftoverLength + length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length());
        }
        if (index < leftoverLength) {
            //noinspection DataFlowIssue
            return (leftover[index]);
        }
        //noinspection DataFlowIssue
        return (current[index - leftoverLength]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String subSequence(int start, int end) {
        if (start < 0 || end < start || end > length()) {
            throw new IndexOutOfBoundsException("Start: " + start + ", End: " + end + ", Length: " + length());
        }
        if (start == end) {
            return "";
        }
        if (start == 0 && end == length()) {
            return toString();
        }
        if (start < leftoverLength) {
            int leftoverPartEnd = Math.min(end, leftoverLength);
            //noinspection DataFlowIssue
            String leftoverPart = new String(leftover, start, leftoverPartEnd - start);
            if (end <= leftoverPartEnd) {
                return leftoverPart;
            }
            //noinspection DataFlowIssue
            String currentPart = new String(current, 0, end - leftoverLength);
            return leftoverPart + currentPart;
        }
        //noinspection DataFlowIssue
        return new String(current, start - leftoverLength, end - start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (cachedString == null) {
            String currentPart = current != null ? new String(current, 0, length) : "";
            cachedString = leftover != null ? new String(leftover, 0, leftoverLength) + currentPart : currentPart;
        }
        return cachedString;
    }
}
