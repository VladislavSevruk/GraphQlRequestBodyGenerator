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
package com.github.vladislavsevruk.generator.model.graphql.optimization;

import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.generator.GqlModelGenerator;
import com.github.vladislavsevruk.generator.model.graphql.parser.impl.GqlSchemaParserImpl;
import com.github.vladislavsevruk.generator.model.graphql.util.GqlModelPathBuilder;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Task;

/**
 * Performs generate GraphQL models action against objects of type T.
 *
 * @param <T> The type of object which this action accepts.
 */
@NonNullApi
public class GenerateGraphqlModelsAction<T extends Task> implements Action<T> {

    private final GqlModelGenerator modelGenerator = new GqlModelGenerator();
    private final GqlModelGeneratorPluginExtension pluginExtension;
    private final String targetDirPath;

    public GenerateGraphqlModelsAction(GqlModelGeneratorPluginExtension pluginExtension, String targetDirPath) {
        this.pluginExtension = pluginExtension;
        this.targetDirPath = targetDirPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(T task) {
        String srcJavaPath = GqlModelPathBuilder.buildModelSrcJavaPath(targetDirPath);
        ReadWriteFileUtil.recursivelyDelete(srcJavaPath);
        SchemaObjectStorage storage = new GqlSchemaParserImpl(pluginExtension).parseSchema();
        modelGenerator.generate(pluginExtension, targetDirPath, storage);
    }
}
