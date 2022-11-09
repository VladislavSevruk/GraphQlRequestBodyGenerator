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
package com.github.vladislavsevruk.generator.model.graphql.extension;

import com.github.vladislavsevruk.generator.model.graphql.constant.ElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlFloatType;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlIntType;
import org.gradle.api.tasks.SourceSet;

import java.io.File;
import java.util.Iterator;

/**
 * Sets default values for plugin extension to use if no configuration values provided.
 */
public class PluginExtensionConvention {

    /**
     * Sets default values for plugin extension to use if no configuration values provided.
     *
     * @param extension <code>GqlModelGeneratorPluginExtension</code> with plugin configurable values.
     * @param sourceSet project's main <code>SourceSet</code> to use as default resource files location.
     */
    public void setDefaults(GqlModelGeneratorPluginExtension extension, SourceSet sourceSet) {
        extension.getAddJacksonAnnotations().convention(false);
        extension.getEntitiesPostfix().convention("");
        extension.getEntitiesPrefix().convention("");
        Iterator<File> iterator = sourceSet.getResources().getSrcDirs().iterator();
        String rssAbsDir = !iterator.hasNext() ? "" : iterator.next().getAbsolutePath() + File.separator;
        extension.getPathToSchemaFile()
                .convention(String.format("%sgraphql%sschema.graphqls", rssAbsDir, File.separator));
        extension.getTargetPackage().convention("com.github.vladislavsevruk.model");
        extension.getTreatArrayAs().convention(ElementSequence.LIST);
        extension.getTreatFloatAs().convention(GqlFloatType.DOUBLE);
        extension.getTreatIdAs().convention(GqlIntType.STRING);
        extension.getTreatIntAs().convention(GqlIntType.INTEGER);
        extension.getUpdateNamesToJavaStyle().convention(true);
        extension.getUseLombokAnnotations().convention(false);
        extension.getUsePrimitivesInsteadOfWrappers().convention(false);
        extension.getUseStringsInsteadOfEnums().convention(false);
    }
}
