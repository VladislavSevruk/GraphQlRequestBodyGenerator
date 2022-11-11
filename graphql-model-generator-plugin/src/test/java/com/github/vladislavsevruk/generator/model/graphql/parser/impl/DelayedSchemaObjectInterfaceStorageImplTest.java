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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
class DelayedSchemaObjectInterfaceStorageImplTest {

    @Test
    void addForExistentTest() {
        DelayedSchemaObjectInterfaceStorageImpl storage = new DelayedSchemaObjectInterfaceStorageImpl();
        String name = "TestPojo";
        Supplier<SchemaEntity> supplier1 = Mockito.mock(Supplier.class);
        storage.add(name, supplier1);
        Supplier<SchemaEntity> supplier2 = Mockito.mock(Supplier.class);
        storage.add(name, supplier2);
        List<Supplier<SchemaEntity>> suppliers = storage.get(name);
        Assertions.assertNotNull(suppliers);
        Assertions.assertEquals(2, suppliers.size());
        Iterator<Supplier<SchemaEntity>> iterator = suppliers.iterator();
        Assertions.assertEquals(supplier1, iterator.next());
        Assertions.assertEquals(supplier2, iterator.next());
    }

    @Test
    void addForNewTest() {
        DelayedSchemaObjectInterfaceStorageImpl storage = new DelayedSchemaObjectInterfaceStorageImpl();
        String name = "TestPojo";
        Supplier<SchemaEntity> supplier = Mockito.mock(Supplier.class);
        storage.add(name, supplier);
        List<Supplier<SchemaEntity>> suppliers = storage.get(name);
        Assertions.assertNotNull(suppliers);
        Assertions.assertEquals(1, suppliers.size());
        Assertions.assertEquals(supplier, suppliers.iterator().next());
    }

    @Test
    void getNonExistentTest() {
        DelayedSchemaObjectInterfaceStorageImpl storage = new DelayedSchemaObjectInterfaceStorageImpl();
        String name = "TestPojo";
        List<Supplier<SchemaEntity>> suppliers = storage.get(name);
        Assertions.assertNotNull(suppliers);
        Assertions.assertTrue(suppliers.isEmpty());
    }
}
