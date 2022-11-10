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
package com.github.vladislavsevruk.generator.model.graphql.parser.impl;

import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.parser.DelayedSchemaObjectInterfaceStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implementation of <code>DelayedSchemaObjectInterfaceStorage</code>.
 *
 * @see DelayedSchemaObjectInterfaceStorage
 */
public class DelayedSchemaObjectInterfaceStorageImpl implements DelayedSchemaObjectInterfaceStorage {

    private final Map<String, List<Supplier<SchemaEntity>>> delayedSuppliers = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(String name, Supplier<SchemaEntity> delayedInterfaceSupplier) {
        delayedSuppliers.computeIfAbsent(name, key -> new ArrayList<>(3));
        delayedSuppliers.get(name).add(delayedInterfaceSupplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Supplier<SchemaEntity>> get(String name) {
        List<Supplier<SchemaEntity>> suppliers = delayedSuppliers.get(name);
        return suppliers != null ? suppliers : Collections.emptyList();
    }
}
