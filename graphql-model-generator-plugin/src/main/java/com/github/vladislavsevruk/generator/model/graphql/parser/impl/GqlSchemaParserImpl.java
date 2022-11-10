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
import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorageImpl;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.model.graphql.collector.SchemaEntityCollector;
import com.github.vladislavsevruk.generator.model.graphql.collector.impl.SchemaEntityCollectorImpl;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.parser.GqlSchemaObjectParser;
import com.github.vladislavsevruk.generator.model.graphql.parser.GqlSchemaParser;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements <code>GqlSchemaParser</code>.
 *
 * @see GqlSchemaParser
 */
@Log4j2
public class GqlSchemaParserImpl implements GqlSchemaParser {

    private final SchemaEntityCollector collector;
    private final GqlModelGeneratorPluginExtension pluginExtension;

    public GqlSchemaParserImpl(GqlModelGeneratorPluginExtension pluginExtension) {
        this.collector = new SchemaEntityCollectorImpl();
        this.pluginExtension = pluginExtension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemaObjectStorage parseSchema() {
        List<String> entities = collector.collect(pluginExtension.getPathToSchemaFile().get());
        // filter out 'Query' and 'Mutation' types
        entities = filterOutQueryAndMutationDeclarations(entities);
        return parseEntities(entities);
    }

    private List<String> filterOutQueryAndMutationDeclarations(List<String> entities) {
        return entities.stream().filter(entity -> !entity.matches("type\\s+(Query|Mutation)\\s+(?s).+"))
                .collect(Collectors.toList());
    }

    private SchemaObjectStorage parseEntities(List<String> entities) {
        log.info("Parsing entities.");
        SchemaObjectStorageImpl parsedEntitiesStorage = new SchemaObjectStorageImpl();
        GqlSchemaObjectParserProviderImpl provider = new GqlSchemaObjectParserProviderImpl(pluginExtension,
                parsedEntitiesStorage, new DelayedSchemaObjectInterfaceStorageImpl());
        for (String entity : entities) {
            boolean isParsed = false;
            for (GqlSchemaObjectParser parser : provider.getParsers()) {
                if (parser.canParse(entity)) {
                    SchemaObject schemaObject = parser.parse(entity);
                    parsedEntitiesStorage.put(schemaObject.getName(), schemaObject);
                    isParsed = true;
                    break;
                }
            }
            if (!isParsed) {
                log.warn("Cannot parse: {}", entity);
            }
        }
        return parsedEntitiesStorage;
    }
}
