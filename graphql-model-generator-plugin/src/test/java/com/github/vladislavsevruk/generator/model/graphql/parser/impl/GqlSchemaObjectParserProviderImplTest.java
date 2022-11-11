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
package com.github.vladislavsevruk.generator.model.graphql.parser.impl;

import com.github.vladislavsevruk.generator.java.storage.SchemaObjectStorage;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.parser.DelayedSchemaObjectInterfaceStorage;
import com.github.vladislavsevruk.generator.model.graphql.parser.GqlSchemaObjectParser;
import com.github.vladislavsevruk.generator.model.graphql.test.data.extension.TestExtensionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class GqlSchemaObjectParserProviderImplTest {

    @Test
    void getParsersTest() {
        GqlModelGeneratorPluginExtension pluginExtension = TestExtensionUtil.mockPluginExtension();
        SchemaObjectStorage schemaObjectStorage = Mockito.mock(SchemaObjectStorage.class);
        DelayedSchemaObjectInterfaceStorage interfaceStorage = Mockito.mock(DelayedSchemaObjectInterfaceStorage.class);
        GqlSchemaObjectParserProviderImpl provider = new GqlSchemaObjectParserProviderImpl(pluginExtension,
                schemaObjectStorage, interfaceStorage);
        List<GqlSchemaObjectParser> parsers = provider.getParsers();
        Assertions.assertNotNull(parsers);
        Assertions.assertEquals(4, parsers.size());
        Assertions.assertEquals(GqlSchemaTypeParser.class, parsers.get(0).getClass());
        Assertions.assertEquals(GqlSchemaInputParser.class, parsers.get(1).getClass());
        Assertions.assertEquals(GqlSchemaEnumParser.class, parsers.get(2).getClass());
        Assertions.assertEquals(GqlSchemaUnionParser.class, parsers.get(3).getClass());
    }
}
