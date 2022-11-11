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

import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.java.util.EntityNameUtil;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.resolver.GqlTypeResolver;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlScalarType;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains common logic for GraphQL complex object parsers.
 */
public abstract class GqlSchemaModelObjectParser extends BaseGqlSchemaObjectParser {

    protected static final String VALUE_TYPE_PATTERN = "\\[?\\s*\\w+\\s*!?\\s*]?\\s*!?";
    private static final Pattern PATTERN = Pattern.compile(
            "(\\w+)\\s*:\\s*(" + VALUE_TYPE_PATTERN + "\\s*)(?>,\\s*)?((?s).*)");

    protected final SchemaObjectStorage customEntitiesStorage;
    protected final GqlTypeResolver gqlTypeResolver;

    protected GqlSchemaModelObjectParser(GqlModelGeneratorPluginExtension pluginExtension,
            SchemaObjectStorage customEntitiesStorage, GqlTypeResolver gqlTypeResolver) {
        super(pluginExtension);
        this.customEntitiesStorage = customEntitiesStorage;
        this.gqlTypeResolver = gqlTypeResolver;
    }

    protected Supplier<SchemaEntity> getDelayedElementTypeSupplier(String customObjectTypeName, boolean isArray) {
        return () -> {
            SchemaObject delayedTypeSchemaObject = customEntitiesStorage.get(customObjectTypeName);
            return gqlTypeResolver.pickJavaType(delayedTypeSchemaObject, isArray);
        };
    }

    protected List<SchemaField> parseFields(String lineToParse) {
        List<SchemaField> gqlSchemaFields = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(lineToParse);
        while (matcher.matches()) {
            String rawSchemaName = matcher.group(1);
            String fieldType = matcher.group(2).trim();
            boolean outerNonNull = fieldType.endsWith("!");
            if (outerNonNull) {
                fieldType = fieldType.substring(0, fieldType.length() - 1).trim();
            }
            boolean isArray = fieldType.startsWith("[") && fieldType.endsWith("]");
            if (isArray) {
                fieldType = fieldType.substring(1, fieldType.length() - 1).trim();
            }
            boolean innerNonNull = fieldType.endsWith("!");
            if (innerNonNull) {
                fieldType = fieldType.substring(0, fieldType.length() - 1).trim();
            }
            String fieldName = modifyFieldName(rawSchemaName);
            gqlSchemaFields.add(
                    generateGqlSchemaField(fieldName, rawSchemaName, fieldType, isArray, outerNonNull || innerNonNull));
            matcher = PATTERN.matcher(matcher.group(3).trim());
        }
        return gqlSchemaFields;
    }

    private SchemaField generateGqlSchemaField(String fieldName, String rawSchemaName, String fieldType,
            boolean isArray, boolean nonNull) {
        GqlScalarType gqlScalarType = GqlScalarType.getByName(fieldType);
        if (gqlScalarType != null) {
            return new GqlSchemaField(fieldName, rawSchemaName, gqlTypeResolver.pickJavaType(gqlScalarType, isArray),
                    nonNull);
        }
        String customObjectTypeName = modifyName(fieldType);
        SchemaObject typeSchemaObject = customEntitiesStorage.get(customObjectTypeName);
        if (typeSchemaObject != null) {
            // resolve straight away
            return new GqlSchemaField(fieldName, rawSchemaName, gqlTypeResolver.pickJavaType(typeSchemaObject, isArray),
                    nonNull);
        }
        // delay resolving
        Supplier<SchemaEntity> delayedElementTypeSupplier = getDelayedElementTypeSupplier(customObjectTypeName,
                isArray);
        return new GqlSchemaField(fieldName, rawSchemaName, delayedElementTypeSupplier, nonNull);
    }

    private String modifyFieldName(String name) {
        return Boolean.TRUE.equals(pluginExtension.getUpdateNamesToJavaStyle().get())
                ? EntityNameUtil.getJavaFormatFieldName(name) : name;
    }
}
