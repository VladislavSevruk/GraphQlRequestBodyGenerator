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
package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Setter
public class TestInputModel {

    @GqlDelegate
    private Map<String, Object> fieldWithDelegateAnnotationWithDelegateMethod;
    @GqlDelegate
    private Map<String, Object> fieldWithDelegateAnnotationWithIgnoredMethod;
    @GqlDelegate
    private Map<String, Object> fieldWithDelegateAnnotationWithInputMethod;
    @GqlDelegate
    private Map<String, Object> fieldWithDelegateAnnotationWithMethod;
    @GqlDelegate
    private NestedTestInputModel fieldWithDelegateAnnotationWithoutMethod;
    private Map<String, Object> fieldWithDelegateMethod;
    private Map<String, Object> fieldWithDelegatePrivateMethod;
    @GqlField
    private Map<String, Object> fieldWithFieldAnnotationWithDelegateMethod;
    @GqlField
    private String fieldWithFieldAnnotationWithIgnoredMethod;
    @GqlField
    private String fieldWithFieldAnnotationWithInputMethod;
    @GqlField
    private String fieldWithFieldAnnotationWithMethod;
    @GqlField
    private NestedTestInputModel fieldWithFieldAnnotationWithoutMethod;
    @GqlIgnore
    private Map<String, Object> fieldWithIgnoreAnnotationWithDelegateMethod;
    @GqlIgnore
    private String fieldWithIgnoreAnnotationWithIgnoredMethod;
    @GqlIgnore
    private String fieldWithIgnoreAnnotationWithInputMethod;
    @GqlIgnore
    private String fieldWithIgnoreAnnotationWithMethod;
    @GqlIgnore
    private NestedTestInputModel fieldWithIgnoreAnnotationWithoutMethod;
    private Long fieldWithIgnoredMethod;
    private List<Integer> fieldWithIgnoredPrivateMethod;
    private String fieldWithInputMethod;
    private String fieldWithInputPrivateMethod;
    private String fieldWithMethod;
    @GqlField(name = "customFieldWithOverriddenNameWithDelegateMethod")
    private Map<String, Object> fieldWithOverriddenNameWithDelegateMethod;
    @GqlField(name = "customFieldWithOverriddenNameWithIgnoredMethod")
    private String fieldWithOverriddenNameWithIgnoredMethod;
    @GqlField(name = "customFieldWithOverriddenNameWithInputMethod")
    private String fieldWithOverriddenNameWithInputMethod;
    @GqlField(name = "customFieldWithOverriddenNameWithMethod")
    private String fieldWithOverriddenNameWithMethod;
    @GqlField(name = "customFieldWithOverriddenNameWithoutMethod")
    private NestedTestInputModel fieldWithOverriddenNameWithoutMethod;
    private String fieldWithPrivateMethod;
    private NestedTestInputModel fieldWithoutMethod;

    @GqlDelegate
    public Map<String, Object> fieldWithDelegateMethod() {
        return getNonMatchingValue(fieldWithDelegateMethod);
    }

    @GqlIgnore
    public Long fieldWithIgnoredMethod() {
        return getNonMatchingValue(fieldWithIgnoredMethod);
    }

    @GqlInput(name = "fieldWithInputMethod")
    public String fieldWithInputMethod() {
        return getNonMatchingValue(fieldWithInputMethod);
    }

    public String fieldWithMethod() {
        return getNonMatchingValue(fieldWithMethod);
    }

    @GqlDelegate
    public Map<String, Object> getFieldWithDelegateAnnotationWithDelegateMethod() {
        return getNonMatchingValue(fieldWithDelegateAnnotationWithDelegateMethod);
    }

    @GqlIgnore
    public Map<String, Object> getFieldWithDelegateAnnotationWithIgnoredMethod() {
        return getNonMatchingValue(fieldWithDelegateAnnotationWithIgnoredMethod);
    }

    @GqlInput(name = "fieldWithDelegateAnnotationWithInputMethod")
    public Map<String, Object> getFieldWithDelegateAnnotationWithInputMethod() {
        return getNonMatchingValue(fieldWithDelegateAnnotationWithInputMethod);
    }

    public Map<String, Object> getFieldWithDelegateAnnotationWithMethod() {
        return getNonMatchingValue(fieldWithDelegateAnnotationWithMethod);
    }

    @GqlDelegate
    public Map<String, Object> getFieldWithFieldAnnotationWithDelegateMethod() {
        return getNonMatchingValue(fieldWithFieldAnnotationWithDelegateMethod);
    }

    @GqlIgnore
    public String getFieldWithFieldAnnotationWithIgnoredMethod() {
        return getNonMatchingValue(fieldWithFieldAnnotationWithIgnoredMethod);
    }

    @GqlInput(name = "fieldWithFieldAnnotationWithInputMethod")
    public String getFieldWithFieldAnnotationWithInputMethod() {
        return getNonMatchingValue(fieldWithFieldAnnotationWithInputMethod);
    }

    public String getFieldWithFieldAnnotationWithMethod() {
        return getNonMatchingValue(fieldWithFieldAnnotationWithMethod);
    }

    @GqlDelegate
    public Map<String, Object> getFieldWithIgnoreAnnotationWithDelegateMethod() {
        return getNonMatchingValue(fieldWithIgnoreAnnotationWithDelegateMethod);
    }

    @GqlIgnore
    public String getFieldWithIgnoreAnnotationWithIgnoredMethod() {
        return getNonMatchingValue(fieldWithIgnoreAnnotationWithIgnoredMethod);
    }

    @GqlInput(name = "fieldWithIgnoreAnnotationWithInputMethod")
    public String getFieldWithIgnoreAnnotationWithInputMethod() {
        return getNonMatchingValue(fieldWithIgnoreAnnotationWithInputMethod);
    }

    public String getFieldWithIgnoreAnnotationWithMethod() {
        return getNonMatchingValue(fieldWithIgnoreAnnotationWithMethod);
    }

    @GqlDelegate
    public Map<String, Object> getFieldWithOverriddenNameWithDelegateMethod() {
        return getNonMatchingValue(fieldWithOverriddenNameWithDelegateMethod);
    }

    @GqlIgnore
    public String getFieldWithOverriddenNameWithIgnoredMethod() {
        return getNonMatchingValue(fieldWithOverriddenNameWithIgnoredMethod);
    }

    @GqlInput(name = "customFieldWithOverriddenNameWithInputMethod")
    public String getFieldWithOverriddenNameWithInputMethod() {
        return getNonMatchingValue(fieldWithOverriddenNameWithInputMethod);
    }

    public String getFieldWithOverriddenNameWithMethod() {
        return getNonMatchingValue(fieldWithOverriddenNameWithMethod);
    }

    public TestInputModel initFieldsWithDelegateAnnotation() {
        fieldWithDelegateAnnotationWithDelegateMethod = getMap("fieldWithDelegateAnnotationWithDelegateMethod");
        fieldWithDelegateAnnotationWithIgnoredMethod = getMap("fieldWithDelegateAnnotationWithIgnoredMethod");
        fieldWithDelegateAnnotationWithInputMethod = getMap("fieldWithDelegateAnnotationWithInputMethod");
        fieldWithDelegateAnnotationWithMethod = getMap("fieldWithDelegateAnnotationWithMethod");
        fieldWithDelegateAnnotationWithoutMethod = getNestedTestInputModel("fieldWithDelegateAnnotationWithoutMethod");
        return this;
    }

    public TestInputModel initFieldsWithFieldAnnotation() {
        fieldWithFieldAnnotationWithDelegateMethod = getMap("fieldWithFieldAnnotationWithDelegateMethod");
        fieldWithFieldAnnotationWithIgnoredMethod = "fieldWithFieldAnnotationWithIgnoredMethod";
        fieldWithFieldAnnotationWithInputMethod = "fieldWithFieldAnnotationWithInputMethod";
        fieldWithFieldAnnotationWithMethod = "fieldWithFieldAnnotationWithMethod";
        fieldWithFieldAnnotationWithoutMethod = getNestedTestInputModel("fieldWithFieldAnnotationWithoutMethod");
        return this;
    }

    public TestInputModel initFieldsWithIgnoreAnnotation() {
        fieldWithIgnoreAnnotationWithDelegateMethod = getMap("fieldWithIgnoreAnnotationWithDelegateMethod");
        fieldWithIgnoreAnnotationWithIgnoredMethod = "fieldWithIgnoreAnnotationWithIgnoredMethod";
        fieldWithIgnoreAnnotationWithInputMethod = "fieldWithIgnoreAnnotationWithInputMethod";
        fieldWithIgnoreAnnotationWithMethod = "fieldWithIgnoreAnnotationWithMethod";
        fieldWithIgnoreAnnotationWithoutMethod = getNestedTestInputModel("fieldWithIgnoreAnnotationWithoutMethod");
        return this;
    }

    public TestInputModel initFieldsWithOverriddenName() {
        fieldWithOverriddenNameWithDelegateMethod = getMap("fieldWithOverriddenNameWithDelegateMethod");
        fieldWithOverriddenNameWithIgnoredMethod = "fieldWithOverriddenNameWithIgnoredMethod";
        fieldWithOverriddenNameWithInputMethod = "fieldWithOverriddenNameWithInputMethod";
        fieldWithOverriddenNameWithMethod = "fieldWithOverriddenNameWithMethod";
        fieldWithOverriddenNameWithoutMethod = getNestedTestInputModel("fieldWithOverriddenNameWithoutMethod");
        return this;
    }

    public TestInputModel initFieldsWithoutAnnotations() {
        fieldWithDelegateMethod = getMap("fieldWithDelegateMethod");
        fieldWithDelegatePrivateMethod = getMap("fieldWithDelegatePrivateMethod");
        fieldWithIgnoredMethod = 1L;
        fieldWithIgnoredPrivateMethod = Arrays.asList(2, 3);
        fieldWithInputMethod = "fieldWithInputMethod";
        fieldWithInputPrivateMethod = "fieldWithInputPrivateMethod";
        fieldWithMethod = "fieldWithMethod";
        fieldWithPrivateMethod = "fieldWithPrivateMethod";
        fieldWithoutMethod = getNestedTestInputModel("fieldWithoutMethod");
        return this;
    }

    @GqlDelegate
    public NestedTestInputModel methodWithoutFieldWithDelegateAnnotation() {
        return getNestedTestInputModel("methodWithoutFieldWithDelegateAnnotation");
    }

    @GqlIgnore
    public String methodWithoutFieldWithIgnoreAnnotation() {
        return getRandomValue();
    }

    @GqlInput(name = "methodWithoutFieldWithInputAnnotation")
    public NestedTestInputModel methodWithoutFieldWithInputAnnotation() {
        return getNestedTestInputModel("methodWithoutFieldWithInputAnnotation");
    }

    public String methodWithoutFieldWithoutAnnotations() {
        return getRandomValue();
    }

    @GqlDelegate
    private Map<String, Object> getFieldWithDelegatePrivateMethod() {
        return getNonMatchingValue(fieldWithDelegatePrivateMethod);
    }

    @GqlIgnore
    private List<Integer> getFieldWithIgnoredPrivateMethod() {
        return getNonMatchingValue(fieldWithIgnoredPrivateMethod);
    }

    @GqlInput(name = "fieldWithInputPrivateMethod")
    private String getFieldWithInputPrivateMethod() {
        return getNonMatchingValue(fieldWithInputPrivateMethod);
    }

    private String getFieldWithPrivateMethod() {
        return getNonMatchingValue(fieldWithPrivateMethod);
    }

    private Map<String, Object> getMap(String value) {
        return getMap(value, value);
    }

    private Map<String, Object> getMap(String key, String value) {
        key = "fieldFrom" + key.substring(0, 1).toUpperCase() + key.substring(1);
        return Collections.singletonMap(key, "value from " + value);
    }

    private NestedTestInputModel getNestedTestInputModel(String value) {
        return new NestedTestInputModel().setNestedValue(value);
    }

    private List<Integer> getNonMatchingValue(List<Integer> fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return fieldValue.stream().map(value -> value + 1).collect(Collectors.toList());
    }

    private Long getNonMatchingValue(Long fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return fieldValue + 1;
    }

    private String getNonMatchingValue(String fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Thread.currentThread().getStackTrace()[2].getMethodName() + " method";
    }

    private Map<String, Object> getNonMatchingValue(Map<String, Object> fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return getMap(methodName, methodName + " method");
    }

    private NestedTestInputModel getNonMatchingValue(NestedTestInputModel fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return getNestedTestInputModel(methodName + " method");
    }

    private String getRandomValue() {
        // random value for unexpected GraphQL fields
        return String.valueOf(new Random().nextLong());
    }
}
