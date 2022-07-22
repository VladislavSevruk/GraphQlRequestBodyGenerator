/*
 * MIT License
 *
 * Copyright (c) 2022 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class StringUtilTest {

    @Test
    void isBlankNullTest() {
        Assertions.assertFalse(StringUtil.isNotBlank(null));
    }

    @ParameterizedTest
    @MethodSource("nonBlankValues")
    void isNotBlankTest(String value) {
        Assertions.assertTrue(StringUtil.isNotBlank(value));
    }

    @ParameterizedTest
    @MethodSource("blankValues")
    void isBlankTest(String value) {
        Assertions.assertFalse(StringUtil.isNotBlank(value));
    }

    private static Stream<Arguments> blankValues() {
        return Stream.of(Arguments.of(" "), Arguments.of("\t"), Arguments.of("\n"), Arguments.of("\r"));
    }

    private static Stream<Arguments> nonBlankValues() {
        return Stream.of(Arguments.of("a"), Arguments.of("1"), Arguments.of("!"));
    }
}
