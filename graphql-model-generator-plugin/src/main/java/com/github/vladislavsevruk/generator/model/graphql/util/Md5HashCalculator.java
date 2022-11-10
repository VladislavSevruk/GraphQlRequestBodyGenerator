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

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculates MD5 hash sum and converts it to hex format.
 */
@Slf4j
public final class Md5HashCalculator {

    private static final String ALGORITHM_NAME = "SHA-256";
    private static final char[] HEX_SYMBOLS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    private Md5HashCalculator() {
    }

    /**
     * Calculates MD5 hash sum for received content.
     *
     * @param content <code>String</code> with content to calculate hash sum for.
     * @return <code>String</code> with MD5 hash sum at hex format.
     */
    public static String calculateCheckSum(String content) {
        if (content == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_NAME);
            return calculateCheckSum(messageDigest, content);
        } catch (NoSuchAlgorithmException nsaEx) {
            log.warn("Failed to calculate hash sum", nsaEx);
            return null;
        }
    }

    /**
     * Calculates MD5 hash sum for content of file with received path.
     *
     * @param schemaFilePath <code>String</code> with GraphQL schema file path.
     * @return <code>String</code> with MD5 hash sum at hex format.
     */
    public static String calculateFileCheckSum(String schemaFilePath) {
        File schemaFile = new File(schemaFilePath);
        if (!schemaFile.exists()) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_NAME);
            return calculateFileCheckSum(messageDigest, schemaFile);
        } catch (NoSuchAlgorithmException nsaEx) {
            log.warn("Failed to calculate hash sum", nsaEx);
            return null;
        }
    }

    private static String calculateCheckSum(MessageDigest messageDigest, String content) {
        messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
        return toHexForm(messageDigest.digest());
    }

    private static String calculateFileCheckSum(MessageDigest messageDigest, File schemaFile) {
        try (FileInputStream fileIs = new FileInputStream(schemaFile)) {
            DigestInputStream digestIs = new DigestInputStream(fileIs, messageDigest);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while (numRead != -1) {
                numRead = digestIs.read(buffer);
            }
            return toHexForm(messageDigest.digest());
        } catch (IOException ioEx) {
            log.warn("Failed to calculate file hash sum", ioEx);
            return null;
        }
    }

    private static String toHexForm(byte[] digest) {
        char[] hexValues = new char[digest.length * 2];
        for (int i = 0; i < digest.length; ++i) {
            hexValues[i * 2] = HEX_SYMBOLS[(digest[i] & 0xF0) >> 4];
            hexValues[i * 2 + 1] = HEX_SYMBOLS[digest[i] & 0x0F];
        }
        return new String(hexValues);
    }
}
