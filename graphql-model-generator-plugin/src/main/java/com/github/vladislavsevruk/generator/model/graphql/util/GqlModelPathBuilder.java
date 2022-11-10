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
package com.github.vladislavsevruk.generator.model.graphql.util;

import java.io.File;

/**
 * Builds paths to GraphQL model generation folders and classes.
 */
public final class GqlModelPathBuilder {

    private GqlModelPathBuilder() {
    }

    /**
     * Builds path for java model package.
     *
     * @param targetDirPath <code>String</code> with generation target directory.
     * @param packageName   <code>String</code> with java package.
     * @return <code>String</code> with java model package path.
     */
    public static String buildModelPackagePath(String targetDirPath, String packageName) {
        return prepareModelPackagePath(targetDirPath, packageName).toString();
    }

    /**
     * Builds path for java model class.
     *
     * @param classDir  <code>String</code> with java model class directory.
     * @param className <code>String</code> with java class name.
     * @return <code>String</code> with java model class path.
     */
    public static String buildModelPath(String classDir, String className) {
        return classDir + className + ".java";
    }

    /**
     * Builds path for java model source directory.
     *
     * @param targetDirPath <code>String</code> with generation target directory.
     * @return <code>String</code> with java model target directory.
     */
    public static String buildModelSrcJavaPath(String targetDirPath) {
        return prepareModelSrcJavaPath(targetDirPath).toString();
    }

    /**
     * Builds path for plugin source target directory.
     *
     * @param targetDirPath <code>String</code> with generation target directory.
     * @return <code>String</code> with plugin source target directory.
     */
    public static String buildModelSrcPath(String targetDirPath) {
        return prepareModelSrcPath(targetDirPath).toString();
    }

    private static StringBuilder prepareModelPackagePath(String targetDirPath, String packageName) {
        StringBuilder builder = prepareModelSrcJavaPath(targetDirPath);
        for (String packagePart : packageName.split("\\.")) {
            builder.append(packagePart).append(File.separator);
        }
        return builder;
    }

    private static StringBuilder prepareModelSrcJavaPath(String targetDirPath) {
        return prepareModelSrcPath(targetDirPath).append("java").append(File.separator).append("main")
                .append(File.separator);
    }

    private static StringBuilder prepareModelSrcPath(String targetDirPath) {
        StringBuilder builder = new StringBuilder(targetDirPath);
        if (!targetDirPath.endsWith(File.separator)) {
            builder.append(File.separator);
        }
        return builder.append("generated").append(File.separator).append("sources").append(File.separator)
                .append("graphqlModels").append(File.separator);
    }
}
