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
import com.github.vladislavsevruk.generator.java.type.SchemaField;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.resolver.GqlSchemaInputResolver;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaInput;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses string with GraphQL object input entity.
 */
public class GqlSchemaInputParser extends GqlSchemaModelObjectParser {

    private static final String TYPE_NAME_PATTERN = "input\\s+(\\w+)";
    private static final Pattern PATTERN = Pattern.compile(TYPE_NAME_PATTERN + "\\s*\\{\\s*"
            + "((?>\\w+\\s*:\\s*" + VALUE_TYPE_PATTERN + "\\s*,?\\s*)+)}");

    public GqlSchemaInputParser(GqlModelGeneratorPluginExtension pluginExtension,
            SchemaObjectStorage customEntitiesStorage) {
        super(pluginExtension, customEntitiesStorage, new GqlSchemaInputResolver(pluginExtension));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canParse(String entity) {
        return PATTERN.matcher(entity).matches();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GqlSchemaInput parse(String entity) {
        Matcher matcher = PATTERN.matcher(entity);
        if (!matcher.matches()) {
            throw new GqlEntityParsingException("Cannot parse to input entity: " + entity);
        }
        String name = modifyName(matcher.group(1));
        List<SchemaField> fields = parseFields(matcher.group(2).trim());
        return new GqlSchemaInput(pluginExtension.getTargetPackage().get(), name, fields);
    }
}
