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
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.BaseSchemaElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates java lines for GqlField annotation if required for received GraphQL schema field.
 */
public class GqlFieldAnnotationGenerator implements FieldAnnotationGenerator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(JavaClassGeneratorConfig config, SchemaField field) {
        if (!GqlSchemaField.class.isAssignableFrom(field.getClass()) || isSimpleUnion((GqlSchemaField) field)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        GqlSchemaField gqlField = (GqlSchemaField) field;
        stringBuilder.append(config.getIndent().value()).append("@GqlField");
        String nonNullParameter = gqlField.isNonNull() ? "nonNull = true" : "";
        String withSelectionSetParameter = !gqlField.isUnion() && hasSelectionSet(gqlField.getType())
                ? "withSelectionSet = true" : "";
        String rawSchemaName = gqlField.getRawSchemaName();
        String nameParameter = rawSchemaName.equals(field.getName()) ? ""
                : String.format("name = \"%s\"", rawSchemaName);
        String annotationParameters = Stream.of(nonNullParameter, withSelectionSetParameter, nameParameter)
                .filter(value -> !value.isEmpty()).collect(Collectors.joining(", "));
        if (!annotationParameters.isEmpty()) {
            stringBuilder.append("(").append(annotationParameters).append(")");
        }
        return stringBuilder.append("\n").toString();
    }

    private boolean hasSelectionSet(SchemaEntity type) {
        if (isElementSequence(type)) {
            return hasSelectionSet(((BaseSchemaElementSequence) type).getElementTypes().iterator().next());
        }
        return !isScalar(type) && !isEnum(type);
    }

    private boolean isElementSequence(SchemaEntity type) {
        return BaseSchemaElementSequence.class.isAssignableFrom(type.getClass());
    }

    private boolean isEnum(SchemaEntity schemaEntity) {
        return GqlSchemaEnum.class.isAssignableFrom(schemaEntity.getClass());
    }

    private boolean isScalar(SchemaEntity schemaEntity) {
        return schemaEntity.getPackage() == null || "java.math".equals(schemaEntity.getPackage());
    }

    private boolean isSimpleUnion(GqlSchemaField schemaField) {
        return schemaField.isUnion() && !schemaField.isNonNull() && schemaField.getRawSchemaName()
                .equals(schemaField.getName());
    }
}
