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

import com.github.vladislavsevruk.generator.java.util.EntityNameUtil;
import com.github.vladislavsevruk.generator.model.graphql.exception.GqlEntityParsingException;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlSchemaEnum;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses string with GraphQL enum entity.
 */
public class GqlSchemaEnumParser extends BaseGqlSchemaObjectParser {

    private static final Pattern PATTERN = Pattern.compile("enum\\s+(\\w+)\\s*\\{\\s*((?>\\w+\\s*,?\\s*)+)}");

    public GqlSchemaEnumParser(GqlModelGeneratorPluginExtension pluginExtension) {
        super(pluginExtension);
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
    public GqlSchemaEnum parse(String entity) {
        Matcher matcher = PATTERN.matcher(entity);
        if (!matcher.matches()) {
            throw new GqlEntityParsingException("Cannot parse to enum entity: " + entity);
        }
        String name = modifyName(matcher.group(1));
        List<String> values = Arrays.asList(matcher.group(2).trim().split("\\s*,\\s*|\\s+"));
        if (pluginExtension.getUpdateNamesToJavaStyle().get()) {
            name = EntityNameUtil.getJavaFormatClassName(name);
            values = values.stream().map(String::toUpperCase).collect(Collectors.toList());
        }
        return new GqlSchemaEnum(pluginExtension.getTargetPackage().get(), name, values);
    }
}
