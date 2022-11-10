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

import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents java class schema of GraphQL type.
 */
public class GqlSchemaType extends GqlSchemaObject {

    private List<Supplier<SchemaEntity>> delayedInterfaceSuppliers = new ArrayList<>(3);

    public GqlSchemaType(String packageName, String name, List<SchemaEntity> interfaces, List<SchemaField> fields) {
        super(packageName, name, null, interfaces, fields);
    }

    /**
     * Adds supplier of interface schema object to suppliers list associated with this POJO.
     *
     * @param delayedInterfaceSupplier <code>Supplier</code> that provides interface schema object.
     */
    public void addDelayedInterfaceSupplier(Supplier<SchemaEntity> delayedInterfaceSupplier) {
        delayedInterfaceSuppliers.add(delayedInterfaceSupplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SchemaEntity> getInterfaces() {
        if (!delayedInterfaceSuppliers.isEmpty()) {
            List<SchemaEntity> interfaces = super.getInterfaces();
            for (Supplier<SchemaEntity> supplier : delayedInterfaceSuppliers) {
                interfaces.add(supplier.get());
            }
            delayedInterfaceSuppliers = new ArrayList<>();
            return interfaces;
        }
        return super.getInterfaces();
    }
}
