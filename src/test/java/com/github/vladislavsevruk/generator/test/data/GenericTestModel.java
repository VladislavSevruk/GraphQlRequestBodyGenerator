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

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GenericTestModel<T> {

    @GqlField(withSelectionSet = true)
    private Collection<T> collectionEntity;
    @GqlField
    private Collection<T> collectionField;
    @GqlDelegate
    private T fieldWithDelegateAnnotation;
    @GqlField(withSelectionSet = true)
    private T fieldWithEntityAnnotation;
    @GqlField
    private Long fieldWithFieldAnnotation;
    @GqlIgnore
    private Long fieldWithIgnoreAnnotation;
    private Long fieldWithoutAnnotations;
    @GqlField(name = "idField")
    private Long id;
    @GqlField(name = "id")
    private Long idField;
    @GqlField(withSelectionSet = true)
    private List<T> listEntity;
    @GqlField(name = "customNamedEntity", withSelectionSet = true)
    private T namedEntity;
    @GqlField(name = "customNamedField")
    private Long namedField;
    @GqlField(name = "customNamedNonNullEntity", nonNull = true, withSelectionSet = true)
    private T namedNonNullEntity;
    @GqlField(name = "customNamedNonNullField", nonNull = true)
    private Long namedNonNullField;
    @GqlField(nonNull = true, withSelectionSet = true)
    private T nonNullEntity;
    @GqlField(nonNull = true)
    private Long nonNullField;
    @GqlField(withSelectionSet = true)
    private Queue<T> queueEntity;
    @GqlField(withSelectionSet = true)
    private Set<T> setEntity;
}
