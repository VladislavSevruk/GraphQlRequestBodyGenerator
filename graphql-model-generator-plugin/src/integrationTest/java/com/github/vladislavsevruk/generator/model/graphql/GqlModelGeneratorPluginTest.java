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
import com.github.vladislavsevruk.generator.java.provider.EnumContentGeneratorProvider;
import com.github.vladislavsevruk.generator.java.provider.InterfaceContentGeneratorProvider;
import com.github.vladislavsevruk.generator.java.provider.JavaClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.generator.GqlModelClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.model.graphql.optimization.EagerAndSpec;
import com.github.vladislavsevruk.generator.model.graphql.optimization.GenerateGraphqlModelsAction;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskOutputs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class GqlModelGeneratorPluginTest {

    @Test
    void applyTest() {
        Project project = Mockito.mock(Project.class);
        SourceSet sourceSet = mockMainSourceSet(project);
        mockExtension(project);
        String targetPath = mockTargetPath(project);
        mockSourceDirectorySet(sourceSet, targetPath);
        mockGraphqlModelsTaskCreation(project);
        Assertions.assertDoesNotThrow(() -> new GqlModelGeneratorPlugin().apply(project));
        verifyGeneratorProviders();
    }

    private void mockExtension(Project project) {
        GqlModelGeneratorPluginExtension extension = TestExtensionUtil.mockPluginExtension();
        ExtensionContainer extensionContainer = Mockito.mock(ExtensionContainer.class);
        when(extensionContainer.create("graphqlModelGenerator", GqlModelGeneratorPluginExtension.class)).thenReturn(
                extension);
        when(project.getExtensions()).thenReturn(extensionContainer);
    }

    @SuppressWarnings("unchecked")
    private void mockGraphqlModelsTaskCreation(Project project) {
        Task task = Mockito.mock(Task.class);
        when(task.doLast(any(GenerateGraphqlModelsAction.class))).thenReturn(task);
        when(project.task("generateGraphqlModels")).thenReturn(task);
        TaskOutputs taskOutputs = Mockito.mock(TaskOutputs.class);
        doNothing().when(taskOutputs).cacheIf(any(Spec.class));
        doNothing().when(taskOutputs).upToDateWhen(any(EagerAndSpec.class));
        when(task.getOutputs()).thenReturn(taskOutputs);
        TaskContainer taskContainer = Mockito.mock(TaskContainer.class);
        Task compileTask = Mockito.mock(Task.class);
        when(compileTask.dependsOn("generateGraphqlModels")).thenReturn(compileTask);
        when(taskContainer.getByName("compileJava")).thenReturn(compileTask);
        when(project.getTasks()).thenReturn(taskContainer);
    }

    private SourceSet mockMainSourceSet(Project project) {
        SourceSet sourceSet = Mockito.mock(SourceSet.class);
        SourceSetContainer sourceSetContainer = Mockito.mock(SourceSetContainer.class);
        when(sourceSetContainer.getByName(SourceSet.MAIN_SOURCE_SET_NAME)).thenReturn(sourceSet);
        JavaPluginConvention javaPluginConvention = Mockito.mock(JavaPluginConvention.class);
        when(javaPluginConvention.getSourceSets()).thenReturn(sourceSetContainer);
        Convention convention = Mockito.mock(Convention.class);
        when(convention.getPlugin(JavaPluginConvention.class)).thenReturn(javaPluginConvention);
        when(project.getConvention()).thenReturn(convention);
        SourceDirectorySet sourceDirectorySet = Mockito.mock(SourceDirectorySet.class);
        File rssDir = new File("test" + File.separator + "rss" + File.separator + "dir");
        when(sourceDirectorySet.getSrcDirs()).thenReturn(Collections.singleton(rssDir));
        when(sourceSet.getResources()).thenReturn(sourceDirectorySet);
        return sourceSet;
    }

    @SuppressWarnings("unchecked")
    private void mockSourceDirectorySet(SourceSet sourceSet, String targetPath) {
        SourceDirectorySet sourceDirectorySet = Mockito.mock(SourceDirectorySet.class);
        String modelDirPath =
                String.join(File.separator, targetPath, "generated", "sources", "graphqlModels", "java", "main")
                        + File.separator;
        when(sourceDirectorySet.srcDir(modelDirPath)).thenReturn(sourceDirectorySet);
        when(sourceSet.java(any(Action.class))).then(invocation -> {
            ((Action<? super SourceDirectorySet>) invocation.getArguments()[0]).execute(sourceDirectorySet);
            return null;
        });
    }

    private String mockTargetPath(Project project) {
        String targetPath = "test" + File.separator + "target" + File.separator + "path";
        when(project.getBuildDir()).thenReturn(new File(targetPath));
        return targetPath;
    }

    private void verifyGeneratorProviders() {
        List<JavaClassContentGeneratorProvider> generatorProviders = ClassGenerationContextManager.getContext()
                .getClassContentGeneratorProviderStorage().getAll();
        Assertions.assertNotNull(generatorProviders);
        Assertions.assertEquals(4, generatorProviders.size());
        Assertions.assertEquals(EnumContentGeneratorProvider.class, generatorProviders.get(0).getClass());
        Assertions.assertEquals(InterfaceContentGeneratorProvider.class, generatorProviders.get(1).getClass());
        Assertions.assertEquals(GqlModelClassContentGeneratorProvider.class, generatorProviders.get(2).getClass());
        Assertions.assertEquals(ClassContentGeneratorProvider.class, generatorProviders.get(3).getClass());
    }
}
