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
package com.github.vladislavsevruk.generator.model.graphql;

import com.github.vladislavsevruk.generator.java.context.ClassGenerationContextManager;
import com.github.vladislavsevruk.generator.java.provider.ClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.extension.PluginExtensionConvention;
import com.github.vladislavsevruk.generator.model.graphql.generator.GqlModelClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.model.graphql.optimization.ConfigurationUpToDateSpec;
import com.github.vladislavsevruk.generator.model.graphql.optimization.EagerAndSpec;
import com.github.vladislavsevruk.generator.model.graphql.optimization.GenerateGraphqlModelsAction;
import com.github.vladislavsevruk.generator.model.graphql.optimization.SchemaUpToDateSpec;
import com.github.vladislavsevruk.generator.model.graphql.util.GqlModelPathBuilder;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.SourceSet;

/**
 * Gradle plugin for automatic GraphQL models generation.
 */
@NonNullApi
public class GqlModelGeneratorPlugin implements Plugin<Project> {

    private static final String COMPILE_JAVA_TASK_NAME = "compileJava";
    private static final String EXTENSION_NAME = "graphqlModelGenerator";
    private static final String TASK_NAME = "generateGraphqlModels";

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Project project) {
        SourceSet sourceSet = getMainSourceSet(project);
        GqlModelGeneratorPluginExtension extension = createExtension(project, sourceSet);
        String targetPath = project.getBuildDir().getPath();
        sourceSet.java(set -> set.srcDir(GqlModelPathBuilder.buildModelSrcJavaPath(targetPath)));
        setGqlModelClassContentGenerator();
        createGraphqlModelsTask(project, extension, targetPath);
    }

    private GqlModelGeneratorPluginExtension createExtension(Project project, SourceSet sourceSet) {
        GqlModelGeneratorPluginExtension extension = project.getExtensions()
                .create(EXTENSION_NAME, GqlModelGeneratorPluginExtension.class);
        new PluginExtensionConvention().setDefaults(extension, sourceSet);
        return extension;
    }

    private void createGraphqlModelsTask(Project project, GqlModelGeneratorPluginExtension extension,
            String targetPath) {
        Task generateGraphqlModelsTask = project.task(TASK_NAME)
                .doLast(new GenerateGraphqlModelsAction<>(extension, targetPath));
        generateGraphqlModelsTask.getOutputs().cacheIf(task -> true);
        generateGraphqlModelsTask.getOutputs().upToDateWhen(getUpToDateSpec(extension, targetPath));
        project.getTasks().getByName(COMPILE_JAVA_TASK_NAME).dependsOn(TASK_NAME);
    }

    private SourceSet getMainSourceSet(Project project) {
        JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
        return javaPlugin.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
    }

    private <T extends Task> Spec<T> getUpToDateSpec(GqlModelGeneratorPluginExtension extension, String targetPath) {
        return new EagerAndSpec<>(new SchemaUpToDateSpec<>(extension.getPathToSchemaFile(), targetPath),
                new ConfigurationUpToDateSpec<>(extension, targetPath));
    }

    private void setGqlModelClassContentGenerator() {
        ClassGenerationContextManager.getContext().getClassContentGeneratorProviderStorage()
                .addBefore(new GqlModelClassContentGeneratorProvider(), ClassContentGeneratorProvider.class);
    }
}