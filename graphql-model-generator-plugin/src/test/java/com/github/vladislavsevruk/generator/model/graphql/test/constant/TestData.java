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
package com.github.vladislavsevruk.generator.model.graphql.test.constant;

import com.github.vladislavsevruk.generator.java.util.FileUtil;
import com.github.vladislavsevruk.generator.model.graphql.util.ReadWriteFileUtil;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestData {

    private static final String TEST_DIR_PATH = String.format("src%stest%<sresources%<s", File.separator);
    private static final String TEST_TEMP_DIR_PATH = String.format("%stemp%s", TEST_DIR_PATH, File.separator);

    private TestData() {
    }

    public static void createTempTestRssDir() {
        FileUtil.recursiveMkdir(tempTestRssDirPath());
        assertTrue(new File(tempTestRssDirPath()).exists());
    }

    public static void removeTempTestRssDir() {
        File tempDir = new File(tempTestRssDirPath());
        if (tempDir.exists()) {
            ReadWriteFileUtil.recursivelyDelete(tempDir);
        }
        assertFalse(tempDir.exists());
    }

    public static String tempTestRssDirPath() {
        return TEST_TEMP_DIR_PATH;
    }

    public static String tempTestRssPath(String filePath) {
        return tempTestRssDirPath() + filePath;
    }
}
