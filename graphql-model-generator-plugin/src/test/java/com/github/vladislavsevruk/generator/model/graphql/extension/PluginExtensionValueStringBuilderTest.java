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
package com.github.vladislavsevruk.generator.model.graphql.extension;

import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.SimpleExtension;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginExtensionValueStringBuilderTest {

    @Test
    void buildTest() {
        SimpleExtension extension = new SimpleExtension();
        String result = new PluginExtensionValueStringBuilder().build(extension);
        String expectedResult = buildExpectedValue(SimpleExtension.getValuesMap(), SimpleExtension.class);
        assertEquals(expectedResult, result);
    }

    private String buildExpectedValue(Map<String, Object> valueMap, Class<?> clazz) {
        return Arrays.stream(clazz.getMethods()).map(Method::getName).filter(valueMap::containsKey)
                .map(methodName -> methodName + ":" + valueMap.get(methodName)).collect(Collectors.joining(","));
    }
}
