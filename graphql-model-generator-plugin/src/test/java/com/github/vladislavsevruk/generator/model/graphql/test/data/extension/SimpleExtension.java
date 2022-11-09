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
package com.github.vladislavsevruk.generator.model.graphql.test.data.extension;

import org.gradle.api.provider.Property;

import java.util.HashMap;
import java.util.Map;

public class SimpleExtension {

    private static final Map<String, Object> VALUES_MAP = initValuesMap();

    public static Map<String, Object> getValuesMap() {
        return VALUES_MAP;
    }

    public Property<Boolean> getFlag() {
        return TestExtensionUtil.mockProperty(true);
    }

    public Property<String> methodWithArgs(String value) {
        throw shouldNotBeCalledException();
    }

    public Object nonPropertyMethod() {
        throw shouldNotBeCalledException();
    }

    public Property<String> value() {
        return TestExtensionUtil.mockProperty("testValue");
    }

    private static Map<String, Object> initValuesMap() {
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("getFlag", true);
        valuesMap.put("value", "testValue");
        return valuesMap;
    }

    private RuntimeException shouldNotBeCalledException() {
        return new RuntimeException("Shouldn't be called");
    }
}
