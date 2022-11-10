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

import com.github.vladislavsevruk.generator.java.util.FileUtil;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Utility class for read and write data to file.
 */
@Log4j2
public final class ReadWriteFileUtil {

    private ReadWriteFileUtil() {
    }

    /**
     * Reads content from file with received path.
     *
     * @param filePath <code>String</code> with path to file to read data from.
     * @return <code>String</code> with content read from file.
     */
    public static String readFileContent(String filePath) {
        log.debug("Reading content from file {}", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            log.debug("File {} doesn't exist", file.getAbsolutePath());
            return null;
        }
        try (FileReader fileReader = new FileReader(file)) {
            int read = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (read != -1) {
                char[] buffer = new char[32];
                read = fileReader.read(buffer, 0, buffer.length);
                stringBuilder.append(buffer);
            }
            return stringBuilder.toString().trim();
        } catch (IOException ioEx) {
            log.warn("Failed to read file content", ioEx);
            return null;
        }
    }

    /**
     * Deletes file or directory with all files and subdirectories.
     *
     * @param filePath <code>String</code> with path to file/directory to delete.
     */
    public static void recursivelyDelete(String filePath) {
        recursivelyDelete(new File(filePath));
    }

    /**
     * Deletes file or directory with all files and subdirectories.
     *
     * @param file <code>File</code> to delete.
     */
    public static void recursivelyDelete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            Optional.ofNullable(file.listFiles()).ifPresent(files -> {
                for (File childFile : files) {
                    recursivelyDelete(childFile);
                }
            });
        }
        try {
            Files.delete(file.toPath());
        } catch (IOException ioEx) {
            log.warn(String.format("%s wasn't removed", file.getAbsolutePath()), ioEx);
        }
    }

    /**
     * Rewrites content of file by received content.
     *
     * @param filePath   <code>String</code> with path to file.
     * @param newContent <code>String</code> with content to write to file.
     */
    public static void replaceFileContent(String filePath, String newContent) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            FileUtil.recursiveMkdir(file.getParent());
        }
        FileUtil.writeToFile(file, newContent);
    }
}
