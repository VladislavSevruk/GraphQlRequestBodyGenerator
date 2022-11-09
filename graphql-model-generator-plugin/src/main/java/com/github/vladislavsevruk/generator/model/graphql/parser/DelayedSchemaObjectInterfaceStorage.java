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
package com.github.vladislavsevruk.generator.model.graphql.parser;

import com.github.vladislavsevruk.generator.java.type.SchemaEntity;

import java.util.List;
import java.util.function.Supplier;

/**
 * Stores interface schema object suppliers for specific POJO models.
 */
public interface DelayedSchemaObjectInterfaceStorage {

    /**
     * Adds supplier of interface schema object to suppliers list associated with received POJO name.
     *
     * @param name                     <code>String</code> with POJO name.
     * @param delayedInterfaceSupplier <code>Supplier</code> that provides interface schema object.
     */
    void add(String name, Supplier<SchemaEntity> delayedInterfaceSupplier);

    /**
     * Returns list of suppliers that provide interface schema objects associated with received POJO name.
     *
     * @param name <code>String</code> with POJO name.
     * @return <code>List</code> of <code>Supplier</code> that provide interface schema objects.
     */
    List<Supplier<SchemaEntity>> get(String name);
}
