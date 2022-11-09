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
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads GraphQL entities from schema file.
 */
public class SchemaEntityReader implements AutoCloseable, Iterator<String> {

    private static final String DIRECTIVE_PATTERN = "(?>\\s*@\\s*\\w+(?>\\s*\\([^)]*\\))?)";
    private static final Pattern ENTITY_TYPE_PATTERN = Pattern.compile(
            "^\\s*(?>type|input|enum|interface)\\s+\\w+" + DIRECTIVE_PATTERN
                    + "?(?>\\s*implements\\s*\\w+\\s*)?\\s*\\{[^}]+}");
    private static final Pattern UNION_PATTERN = Pattern.compile(
            "^\\s*union\\s+\\w+" + DIRECTIVE_PATTERN + "?\\s*=\\s*\\w+(\\s*\\|\\s*\\w+)*");
    private final Scanner scanner;
    private String nextEntity = null;

    public SchemaEntityReader(File file) throws FileNotFoundException {
        scanner = new Scanner(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        scanner.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        while (!isEntityFound(nextEntity)) {
            if (!scanner.hasNextLine()) {
                return false;
            }
            nextEntity = findEntity();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String next() {
        if (Objects.nonNull(nextEntity)) {
            nextEntity = removeNonEntityAtStart(nextEntity);
        }
        return isUnion(nextEntity) ? nextUnion(nextEntity) : removeNonEntityAtEnd(nextEntity);
    }

    private String findEntity() {
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = removeExcessiveInformation(scanner.nextLine()).trim();
            if (line.isEmpty()) {
                continue;
            }
            stringBuilder.append(line).append("\n");
            String collectedValue = stringBuilder.toString();
            if (isEntityFound(collectedValue)) {
                return removeNonEntityAtStart(collectedValue);
            }
        }
        return null;
    }

    private boolean hasSeveralUnions(String line) {
        Matcher matcher = UNION_PATTERN.matcher(line);
        if (!matcher.find()) {
            return false;
        }
        return matcher.region(matcher.end(), line.length()).find();
    }

    private boolean isEntityFound(String line) {
        return line != null && (isUnion(line) || isEntityType(line));
    }

    private boolean isEntityType(String line) {
        return ENTITY_TYPE_PATTERN.matcher(line).find();
    }

    private boolean isUnion(String line) {
        return UNION_PATTERN.matcher(line).find();
    }

    private String nextUnion(String line) {
        StringBuilder builder = new StringBuilder(line);
        while (scanner.hasNextLine() && !isEntityType(line) && !hasSeveralUnions(line)) {
            String newLine = removeExcessiveInformation(scanner.nextLine()).trim();
            if (!newLine.isEmpty()) {
                line = builder.append(newLine).append("\n").toString();
            }
        }
        return removeNonEntityAtEnd(line);
    }

    private String removeComments(String line) {
        int commentStartIndex = line.indexOf('#');
        if (commentStartIndex != -1) {
            return line.substring(0, commentStartIndex).trim();
        }
        return line;
    }

    private String removeDirectives(String line) {
        return line.replaceAll(DIRECTIVE_PATTERN, "").replaceAll("\\s+", " ");
    }

    private String removeExcessiveInformation(String line) {
        return removeDirectives(removeComments(line));
    }

    private String removeNonEntityAtEnd(String line) {
        Matcher matcher = UNION_PATTERN.matcher(line);
        if (matcher.find()) {
            int end = matcher.end();
            nextEntity = line.substring(end);
            return line.substring(0, end).trim();
        }
        matcher = ENTITY_TYPE_PATTERN.matcher(line);
        if (matcher.find()) {
            int end = matcher.end();
            nextEntity = line.substring(end);
            return line.substring(0, end).trim();
        }
        return line.trim();
    }

    private String removeNonEntityAtStart(String line) {
        Matcher matcher = UNION_PATTERN.matcher(line);
        if (matcher.find()) {
            int start = matcher.start();
            return line.substring(start);
        }
        matcher = ENTITY_TYPE_PATTERN.matcher(line);
        if (matcher.find()) {
            int start = matcher.start();
            return line.substring(start);
        }
        return line;
    }
}
