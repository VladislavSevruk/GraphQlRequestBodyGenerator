/*
 * MIT License
 *
 * Copyright (c) 2020 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.strategy.marker;

/**
 * Manages model fields marking strategies for input and selection set.
 *
 * @see FieldMarkingStrategyManager
 */
public final class FieldMarkingStrategySourceManager {

    private static final FieldMarkingStrategyManager INPUT_MANAGER = new FieldMarkingStrategyManagerImpl();
    private static final FieldMarkingStrategyManager SELECTION_SET_MANAGER = new FieldMarkingStrategyManagerImpl();

    private FieldMarkingStrategySourceManager() {
    }

    /**
     * Returns {@link FieldMarkingStrategyManager} for inputs.
     */
    public static FieldMarkingStrategyManager input() {
        return INPUT_MANAGER;
    }

    /**
     * Returns {@link FieldMarkingStrategyManager} for selection sets.
     */
    public static FieldMarkingStrategyManager selectionSet() {
        return SELECTION_SET_MANAGER;
    }
}
