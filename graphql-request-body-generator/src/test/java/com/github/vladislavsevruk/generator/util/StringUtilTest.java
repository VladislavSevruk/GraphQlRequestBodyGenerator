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
