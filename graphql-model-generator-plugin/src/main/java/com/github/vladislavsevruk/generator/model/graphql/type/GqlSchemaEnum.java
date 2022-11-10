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
package com.github.vladislavsevruk.generator.model.graphql.type;

import com.github.vladislavsevruk.generator.java.type.EnumConstantSchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaEnum;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents java class schema of GraphQL enum.
 */
public final class GqlSchemaEnum implements SchemaEnum {

    @Getter
    private final List<SchemaField> fields;
    @Getter
    private final String name;
    private final String packageName;

    public GqlSchemaEnum(String packageName, String name, List<String> fields) {
        this.packageName = packageName;
        this.name = name;
        this.fields = fields.stream().map(EnumConstantSchemaField::new).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SchemaEntity> getInterfaces() {
        // no interfaces for GraphQL enums
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return packageName;
    }
}
