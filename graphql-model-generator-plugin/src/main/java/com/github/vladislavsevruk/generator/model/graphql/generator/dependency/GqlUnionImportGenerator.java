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
package com.github.vladislavsevruk.generator.model.graphql.generator.dependency;

import com.github.vladislavsevruk.generator.java.config.JavaClassGeneratorConfig;
import com.github.vladislavsevruk.generator.java.generator.ClassElementCollectionGenerator;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Generates java import lines for GqlUnion and GqlUnionType annotations if required for received GraphQL schema object.
 */
public class GqlUnionImportGenerator implements ClassElementCollectionGenerator {

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> generate(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        if (schemaObject.getFields().stream().anyMatch(this::isUnion)) {
            return Arrays.asList("import com.github.vladislavsevruk.generator.annotation.GqlUnion;\n",
                    "import com.github.vladislavsevruk.generator.annotation.GqlUnionType;\n");
        }
        return Collections.emptyList();
    }

    private boolean isUnion(SchemaField schemaField) {
        return GqlSchemaField.class.isAssignableFrom(schemaField.getClass())
                && ((GqlSchemaField) schemaField).isUnion();
    }
}
