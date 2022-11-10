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
package com.github.vladislavsevruk.generator.model.graphql.generator.field;

import com.github.vladislavsevruk.generator.java.config.JavaClassGeneratorConfig;
import com.github.vladislavsevruk.generator.java.generator.FieldAnnotationGenerator;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaUnionType;

import java.util.Arrays;

/**
 * Generates java lines for GqlUnion and GqlUnionType annotations if required for received GraphQL schema field.
 */
public class GqlUnionAnnotationGenerator implements FieldAnnotationGenerator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(JavaClassGeneratorConfig config, SchemaField field) {
        if (!GqlSchemaField.class.isAssignableFrom(field.getClass()) || !((GqlSchemaField) field).isUnion()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        GqlSchemaField gqlUnionField = (GqlSchemaField) field;
        stringBuilder.append(config.getIndent().value()).append("@GqlUnion(");
        String[] unionTypes = getUnionTypes(gqlUnionField);
        if (unionTypes.length == 1) {
            stringBuilder.append(unionTypes[0]);
        } else {
            String annotationParameters = String.join(", ", unionTypes);
            stringBuilder.append("{ ").append(annotationParameters).append(" }");
        }
        return stringBuilder.append(")\n").toString();
    }

    private String getUnionType(GqlSchemaUnionType gqlGqlSchemaUnionType) {
        StringBuilder stringBuilder = new StringBuilder("@GqlUnionType(");
        if (!gqlGqlSchemaUnionType.getType().getName().equals(gqlGqlSchemaUnionType.getRawSchemaName())) {
            stringBuilder.append("value = ").append(gqlGqlSchemaUnionType.getType().getName())
                    .append(".class, name = \"").append(gqlGqlSchemaUnionType.getRawSchemaName()).append("\"");
        } else {
            stringBuilder.append(gqlGqlSchemaUnionType.getType().getName()).append(".class");
        }
        return stringBuilder.append(")").toString();
    }

    private String[] getUnionTypes(GqlSchemaField gqlUnionField) {
        return Arrays.stream(gqlUnionField.getUnionTypes()).map(this::getUnionType).toArray(String[]::new);
    }
}
