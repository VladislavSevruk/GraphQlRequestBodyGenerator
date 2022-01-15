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
import com.github.vladislavsevruk.generator.annotation.GqlFieldArgument;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class TestModel {

    @GqlField(withSelectionSet = true)
    private Collection<NestedTestModel> collectionEntity;
    @GqlField
    private Collection<NestedTestModel> collectionField;
    @GqlField(withSelectionSet = true)
    private NestedTestModel entity;
    @GqlField(withSelectionSet = true, alias = "aliasForEntityWithAlias")
    private NestedTestModel entityWithAlias;
    @GqlField(withSelectionSet = true, alias = "aliasForEntityWithAliasAndArgument",
            arguments = @GqlFieldArgument(name = "argumentForEntityWithAliasAndArgument",
                    value = "\"valueForEntityWithAliasAndArgument\""))
    private NestedTestModel entityWithAliasAndArgument;
    @GqlField(withSelectionSet = true, arguments = @GqlFieldArgument(name = "argumentForEntityWithArgument",
            value = "\"valueForEntityWithArgument\""))
    private NestedTestModel entityWithArgument;
    @GqlField(alias = "aliasForFieldWithAlias")
    private Long fieldWithAlias;
    @GqlField(alias = "aliasForFieldWithAliasAndArguments",
            arguments = { @GqlFieldArgument(name = "argumentForFieldWithAliasAndArguments1", value = "1"),
                    @GqlFieldArgument(name = "argumentForFieldWithAliasAndArguments2", value = "2") })
    private Long fieldWithAliasAndArguments;
    @GqlField(arguments = @GqlFieldArgument(name = "argumentForFieldWithArgument",
            value = "\"valueForFieldWithArgument\""))
    private Long fieldWithArgument;
    @GqlDelegate
    private NestedTestModel fieldWithDelegateAnnotation;
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
    private List<NestedTestModel> listEntity;
    @GqlField(name = "customNamedEntity", withSelectionSet = true)
    private NestedTestModel namedEntity;
    @GqlField(name = "customNamedField")
    private Long namedField;
    @GqlField(name = "customNamedNonNullEntity", nonNull = true, withSelectionSet = true)
    private NestedTestModel namedNonNullEntity;
    @GqlField(name = "customNamedNonNullField", nonNull = true)
    private Long namedNonNullField;
    @GqlField(nonNull = true, withSelectionSet = true)
    private NestedTestModel nonNullEntity;
    @GqlField(nonNull = true)
    private Long nonNullField;
    @GqlField(withSelectionSet = true)
    private Queue<NestedTestModel> queueEntity;
    @GqlField(withSelectionSet = true)
    private Set<NestedTestModel> setEntity;
}
