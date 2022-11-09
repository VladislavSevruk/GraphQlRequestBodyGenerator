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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class GqlFilePathBuilderTest {

    @Test
    void buildModelPackagePathTest() {
        String targetDir = "target";
        String packageName = "com.test";
        String result = GqlModelPathBuilder.buildModelPackagePath(targetDir, packageName);
        String expectedResult = String.format(
                "target%sgenerated%<ssources%<sgraphqlModels%<sjava%<smain%<scom%<stest%<s", File.separator);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void buildModelPathTest() {
        String targetDir = "target" + File.separator;
        String className = "TestClass";
        String result = GqlModelPathBuilder.buildModelPath(targetDir, className);
        String expectedResult = String.format("target%sTestClass.java", File.separator);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void buildModelSrcJavaPathTest() {
        String targetDir = "target";
        String result = GqlModelPathBuilder.buildModelSrcJavaPath(targetDir);
        String expectedResult = String.format("target%sgenerated%<ssources%<sgraphqlModels%<sjava%<smain%<s",
                File.separator);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void buildModelSrcPathTest() {
        String targetDir = "target";
        String result = GqlModelPathBuilder.buildModelSrcPath(targetDir);
        String expectedResult = String.format("target%sgenerated%<ssources%<sgraphqlModels%<s", File.separator);
        Assertions.assertEquals(expectedResult, result);
    }
}
