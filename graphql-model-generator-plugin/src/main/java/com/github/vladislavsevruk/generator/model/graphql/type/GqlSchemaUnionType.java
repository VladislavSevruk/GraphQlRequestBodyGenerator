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
import lombok.Getter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents GraphQL union type meta information.
 */
public class GqlSchemaUnionType {

    @Getter
    private final String rawSchemaName;
    private Supplier<SchemaEntity> delayedTypeSupplier;
    private SchemaEntity type;

    public GqlSchemaUnionType(String rawSchemaName, SchemaEntity type) {
        this.rawSchemaName = rawSchemaName;
        this.type = type;
    }

    public GqlSchemaUnionType(String rawSchemaName, Supplier<SchemaEntity> delayedTypeSupplier) {
        Objects.requireNonNull(delayedTypeSupplier, "Delayed type initialization cannot be null.");
        this.rawSchemaName = rawSchemaName;
        this.delayedTypeSupplier = delayedTypeSupplier;
    }

    /**
     * Returns <code>SchemaEntity</code> of this union type.
     */
    public SchemaEntity getType() {
        if (this.type == null && this.delayedTypeSupplier != null) {
            this.type = this.delayedTypeSupplier.get();
        }
        return this.type;
    }
}
