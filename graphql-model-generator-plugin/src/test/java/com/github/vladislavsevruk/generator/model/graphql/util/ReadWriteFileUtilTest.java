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

import com.github.vladislavsevruk.generator.model.graphql.test.constant.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadWriteFileUtilTest {

    @BeforeEach
    void createTempDir() {
        TestData.createTempTestRssDir();
    }

    @Test
    void readFileEmptyContentTest() throws IOException {
        String filePath = TestData.tempTestRssPath("readFileEmptyContentTestFile");
        assertTrue(new File(filePath).createNewFile());
        String result = ReadWriteFileUtil.readFileContent(filePath);
        assertEquals("", result);
    }

    @Test
    void readNonExistentFileContentTest() {
        String filePath = TestData.tempTestRssPath("readNonExistentFileContentTestFile");
        String result = ReadWriteFileUtil.readFileContent(filePath);
        assertNull(result);
    }

    @Test
    void recursivelyDeleteNonExistentFileTest() {
        String filePath = TestData.tempTestRssPath("recursivelyDeleteNonExistentFileTestFile");
        assertFalse(new File(filePath).exists());
        assertDoesNotThrow(() -> ReadWriteFileUtil.recursivelyDelete(filePath));
        assertFalse(new File(filePath).exists());
    }

    @Test
    void recursivelyDeleteTest() throws IOException {
        String tempTestDir1Path = TestData.tempTestRssPath("recursivelyDeleteTestFolder-1" + File.separator);
        String file11Path = tempTestDir1Path + "recursivelyDeleteTestFile-11";
        assertTrue(new File(tempTestDir1Path).mkdir());
        assertTrue(new File(file11Path).createNewFile());
        String file12Path = tempTestDir1Path + "recursivelyDeleteTestFile-12";
        assertTrue(new File(file12Path).createNewFile());
        String tempTestDir2Path = TestData.tempTestRssPath("recursivelyDeleteTestFolder-2" + File.separator);
        assertTrue(new File(tempTestDir2Path).mkdir());
        String file21Path = tempTestDir2Path + "recursivelyDeleteTestFile-21";
        assertTrue(new File(file21Path).createNewFile());
        String file22Path = tempTestDir2Path + "recursivelyDeleteTestFile-22";
        assertTrue(new File(file22Path).createNewFile());
        ReadWriteFileUtil.recursivelyDelete(TestData.tempTestRssDirPath());
        assertFalse(new File(TestData.tempTestRssDirPath()).exists());
    }

    @AfterEach
    void removeTempDir() {
        TestData.removeTempTestRssDir();
    }

    @Test
    void replaceFileContentTest() {
        String filePath = TestData.tempTestRssPath("replaceFileContentTestFile");
        String content1 = "1-test-content-1";
        ReadWriteFileUtil.replaceFileContent(filePath, content1);
        String result = ReadWriteFileUtil.readFileContent(filePath);
        assertEquals(content1, result);
        String content2 = "2-test-content-2";
        ReadWriteFileUtil.replaceFileContent(filePath, content2);
        result = ReadWriteFileUtil.readFileContent(filePath);
        assertEquals(content2, result);
    }

    @Test
    void writeToFileAtNonExistentDirTest() {
        String filePath = TestData.tempTestRssPath(
                "writeToFileAtNonExistentDirFolder" + File.separator + "writeToFileAtNonExistentDirTest");
        String content = "test-content";
        ReadWriteFileUtil.replaceFileContent(filePath, content);
        String result = ReadWriteFileUtil.readFileContent(filePath);
        assertEquals(content, result);
    }
}
