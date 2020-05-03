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

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;

import java.util.Collection;

public class NestedTestModelWithAnnotations {

    @GqlField
    private Collection<NestedTestModelWithAnnotations> collectionField;
    @GqlField
    private Long fieldWithFieldAnnotation;
    @GqlIgnore
    private Long fieldWithIgnoreAnnotation;
    private Long fieldWithoutAnnotations;
    @GqlField(name = "idField")
    private Long id;
    @GqlField(name = "id")
    private Long idField;
    @GqlField(name = "customNamedField")
    private Long namedField;
    @GqlField(name = "customNamedNonNullableField", nonNullable = true)
    private Long namedNonNullableField;
    @GqlField(nonNullable = true)
    private Long nonNullableField;
}
