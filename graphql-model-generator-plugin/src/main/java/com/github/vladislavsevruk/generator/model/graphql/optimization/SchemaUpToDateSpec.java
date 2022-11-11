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

import com.github.vladislavsevruk.generator.model.graphql.util.GqlModelPathBuilder;
import com.github.vladislavsevruk.generator.model.graphql.util.HashCalculator;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;
import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.gradle.api.specs.Spec;

/**
 * Specification for checking if GraphQL schema was updated so GraphQL POJO models re-generation is required.
 *
 * @param <T> The target type for this Spec.
 */
public class SchemaUpToDateSpec<T extends Task> implements Spec<T> {

    private final Property<String> pathToSchemaFileProperty;
    private final String targetPath;

    public SchemaUpToDateSpec(Property<String> pathToSchemaFileProperty, String targetPath) {
        this.pathToSchemaFileProperty = pathToSchemaFileProperty;
        this.targetPath = targetPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T element) {
        String schemaHashSum = HashCalculator.calculateFileCheckSum(pathToSchemaFileProperty.get());
        if (schemaHashSum == null) {
            return false;
        }
        String schemaHashSumPath = buildSchemaHashSumPath();
        String cachedSchemaHashSum = ReadWriteFileUtil.readFileContent(schemaHashSumPath);
        if (schemaHashSum.equals(cachedSchemaHashSum)) {
            return true;
        }
        ReadWriteFileUtil.replaceFileContent(schemaHashSumPath, schemaHashSum);
        return false;
    }

    private String buildSchemaHashSumPath() {
        return GqlModelPathBuilder.buildModelSrcPath(targetPath) + ".schemaHashSum";
    }
}
