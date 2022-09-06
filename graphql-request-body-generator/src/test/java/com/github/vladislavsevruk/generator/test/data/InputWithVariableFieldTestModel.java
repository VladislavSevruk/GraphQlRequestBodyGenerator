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
package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlInput;
import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class InputWithVariableFieldTestModel {

    @GqlField
    @GqlVariableType
    private String testField;

    @GqlField
    private String testFieldWithAnnotationValues;

    public String getTestField() {
        return getNonMatchingValue(testField);
    }

    @GqlVariableType
    public String getVariableTypeMethod() {
        return "getVariableTypeMethod";
    }

    @GqlInput(name = "variableTypeInputMethod")
    @GqlVariableType(defaultValue = "\"test\"", isRequired = true, variableName = "variableMethodName",
            variableType = "CustomType")
    public String getVariableTypeInputMethod() {
        return "getVariableTypeInputMethod";
    }

    private String getNonMatchingValue(String fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Thread.currentThread().getStackTrace()[2].getMethodName() + " method";
    }
}
