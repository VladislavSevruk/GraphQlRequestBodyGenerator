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
package com.github.vladislavsevruk.generator.model.graphql.collector.impl;

import com.github.vladislavsevruk.generator.model.graphql.collector.SchemaEntityCollector;
import com.github.vladislavsevruk.generator.model.graphql.util.SchemaEntityReader;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements <code>SchemaEntityCollector</code>.
 *
 * @see SchemaEntityCollector
 */
@Log4j2
public class SchemaEntityCollectorImpl implements SchemaEntityCollector {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> collect(String pathToSchemaFile) {
        File file = new File(pathToSchemaFile);
        if (!file.exists()) {
            log.error("Please check path to schema file.");
            return Collections.emptyList();
        }
        List<String> entities = new ArrayList<>();
        log.info("Collecting entities from schema file.");
        try (SchemaEntityReader schemaEntityReader = new SchemaEntityReader(file)) {
            while (schemaEntityReader.hasNext()) {
                String entity = schemaEntityReader.next();
                entities.add(entity);
            }
            return entities;
        } catch (FileNotFoundException fnfEx) {
            log.error("Schema file wasn't found.", fnfEx);
            return Collections.emptyList();
        }
    }
}
