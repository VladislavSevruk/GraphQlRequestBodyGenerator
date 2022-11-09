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
package com.github.vladislavsevruk.generator.model.graphql.generator;

import com.github.vladislavsevruk.generator.java.JavaClassContentGenerator;
import com.github.vladislavsevruk.generator.java.config.JavaClassGeneratorConfig;
import com.github.vladislavsevruk.generator.java.constant.Indent;
import com.github.vladislavsevruk.generator.java.context.ClassGenerationContextManager;
import com.github.vladislavsevruk.generator.java.picker.ClassContentGeneratorPicker;
import com.github.vladislavsevruk.generator.java.provider.JavaClassContentGeneratorProvider;
import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.java.type.SchemaObject;
import com.github.vladislavsevruk.generator.java.util.FileUtil;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.util.GqlModelPathBuilder;

/**
 * Generates POJO classes according to received schemas and configuration properties.
 */
public class GqlModelGenerator {

    private final ClassContentGeneratorPicker classContentGeneratorPicker;

    public GqlModelGenerator() {
        this(ClassGenerationContextManager.getContext().getClassContentGeneratorPicker());
    }

    public GqlModelGenerator(ClassContentGeneratorPicker classContentGeneratorPicker) {
        this.classContentGeneratorPicker = classContentGeneratorPicker;
    }

    /**
     * Generates POJO classes according to received schemas and configuration properties.
     *
     * @param pluginExtension <code>GqlModelGeneratorPluginExtension</code> with plugin configuration properties.
     * @param targetDirPath   <code>String</code> with POJO classes generation target path.
     * @param storage         <code>SchemaObjectStorage</code> with schema objects to generate.
     */
    public void generate(GqlModelGeneratorPluginExtension pluginExtension, String targetDirPath,
            SchemaObjectStorage storage) {
        JavaClassGeneratorConfig javaClassGeneratorConfig = buildJavaClassGeneratorConfig(pluginExtension,
                targetDirPath);
        storage.getAllObjects().forEach(object -> generateModel(javaClassGeneratorConfig, object));
    }

    private JavaClassGeneratorConfig buildJavaClassGeneratorConfig(GqlModelGeneratorPluginExtension pluginExtension,
            String targetDirPath) {
        return JavaClassGeneratorConfig.builder().addEmptyLineBetweenFields(false)
                .addJacksonAnnotations(pluginExtension.getAddJacksonAnnotations().get())
                .pathToTargetFolder(targetDirPath).sortFieldsByModifiers(false).useIndent(Indent.SPACES_2)
                .useLombokAnnotations(pluginExtension.getUseLombokAnnotations().get()).build();
    }

    private void createModelFile(JavaClassGeneratorConfig config, SchemaObject schemaObject, String javaClassContent) {
        String classDir = GqlModelPathBuilder.buildModelPackagePath(config.getPathToTargetFolder(),
                schemaObject.getPackage());
        FileUtil.recursiveMkdir(classDir);
        String filePath = GqlModelPathBuilder.buildModelPath(classDir, schemaObject.getName());
        FileUtil.writeToNewFile(filePath, javaClassContent);
    }

    private void generateModel(JavaClassGeneratorConfig config, SchemaObject schemaObject) {
        JavaClassContentGeneratorProvider classContentGeneratorProvider
                = classContentGeneratorPicker.pickClassContentGeneratorProvider(schemaObject);
        String javaClassContent = new JavaClassContentGenerator(classContentGeneratorProvider).generate(config,
                schemaObject);
        createModelFile(config, schemaObject, javaClassContent);
    }
}
