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
package com.github.vladislavsevruk.generator.util;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains utility methods for escaped strings generation.
 */
@Log4j2
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * Generates escaped value string for parameter values, e.g.<ul>
     * <li>"literalValue" -&gt; "\"literalValue\""</li>
     * <li>"literal\"With\"Quotes" -&gt; "\"literal\\\"With\\\"Quotes\""</li></ul>
     *
     * @param value <code>String</code> to generated escaped String for.
     * @return <code>String</code> with generated escaped value.
     */
    public static String addQuotesForStringArgument(String value) {
        return String.format("\"%s\"", escapeQuotes(value));
    }

    /**
     * Generates escaped value string for parameter values, e.g.<ul>
     * <li>"literalValue" -&gt; "literalValue"</li>
     * <li>"literal\"With\"Quotes" -&gt; "literal\\\"With\\\"Quotes"</li></ul>
     *
     * @param value <code>String</code> to generated escaped String for.
     * @return <code>String</code> with generated escaped value.
     */
    public static String escapeQuotes(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Generates escaped value string for parameter values, e.g.<ul>
     * <li>escapes quotes for literals:<ul>
     * <li>"literalValue" -&gt; "\"literalValue\""</li>
     * <li>"literal\"With\"Quotes" -&gt; "\"literal\\\"With\\\"Quotes\""</li></ul></li>
     * <li>compose iterables or arrays to string:<ul>
     * <li>[ value1, value2 ] -&gt; "[value1,value2]"</li>
     * <li>[ "literalValue1", "literalValue2" ] -&gt; "[\"literalValue1\",\"literalValue2\"]"</li>
     * <li>[ "literalValue", "literal\"With\"Quotes" ] -&gt; "[\"literalValue\",\"literal\\\"With\\\"Quotes\"]"</li></ul>
     * </ul>
     *
     * @param value <code>Object</code> to generated escaped String for.
     * @return <code>String</code> with generated escaped value.
     */
    public static String generateEscapedValueString(Object value) {
        if (value == null) {
            log.debug("Value is null.");
            return "null";
        }
        Class<?> valueClass = value.getClass();
        if (Iterable.class.isAssignableFrom(valueClass) || valueClass.isArray()) {
            log.debug("Value is iterable or array.");
            // compose all elements through the comma and surround by square brackets
            return "[" + String.join(",", convertToStringList(value)) + "]";
        }
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            log.debug("Value is char sequence.");
            // add escaped quotes for literals
            return addQuotesForStringArgument(value.toString());
        }
        return String.valueOf(value);
    }

    /**
     * Checks if received value contains any non-space symbols.
     *
     * @param value <code>String</code> to check.
     * @return <code>true</code> if received value contains any non-space symbols, <code>false</code> otherwise.
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.replaceAll("\\s", "").isEmpty();
    }

    private static List<String> convertToStringList(Object elements) {
        return StreamUtil.createStream(elements)
                .map(value -> CharSequence.class.isAssignableFrom(value.getClass()) ? addQuotesForStringArgument(
                        value.toString()) : value.toString()).collect(Collectors.toList());
    }
}
