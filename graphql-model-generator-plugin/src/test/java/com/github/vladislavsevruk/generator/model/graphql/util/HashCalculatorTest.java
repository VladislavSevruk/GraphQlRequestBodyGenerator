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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashCalculatorTest {

    @Test
    void calculateCheckSumTest() {
        String content = "testValue";
        String result1 = HashCalculator.calculateCheckSum(content);
        assertTrue(result1.matches("[\\dA-F]{64}"));
        String result2 = HashCalculator.calculateCheckSum(content);
        assertEquals(result1, result2);
    }

    @Test
    void calculateFileCheckSumTest() {
        TestData.createTempTestRssDir();
        String filePath = TestData.tempTestRssPath("calculateFileCheckSumTestFile");
        String content = "testValue";
        try {
            ReadWriteFileUtil.replaceFileContent(filePath, content);
            String result1 = HashCalculator.calculateFileCheckSum(filePath);
            assertNotNull(result1);
            assertTrue(result1.matches("[\\dA-F]{64}"));
            String result2 = HashCalculator.calculateCheckSum(content);
            assertEquals(result1, result2);
        } finally {
            TestData.removeTempTestRssDir();
        }
    }

    @Test
    void calculateNonExistentFileCheckSumTest() {
        String filePath = TestData.tempTestRssPath("calculateNonExistentFileCheckSumTestFile");
        assertNull(HashCalculator.calculateFileCheckSum(filePath));
    }

    @Test
    void calculateNullCheckSumTest() {
        assertNull(HashCalculator.calculateCheckSum(null));
    }
}
