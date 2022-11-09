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
package com.github.vladislavsevruk.generator.model.graphql.generator;

import com.github.vladislavsevruk.generator.java.generator.ClassElementCollectionGenerator;
import com.github.vladislavsevruk.generator.java.generator.FieldAnnotationGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.EqualsMethodImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.FieldTypeImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.InterfaceImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.JacksonImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.LombokImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.dependency.SuperclassImportGenerator;
import com.github.vladislavsevruk.generator.java.generator.field.JacksonFieldAnnotationGenerator;
import com.github.vladislavsevruk.generator.model.graphql.generator.dependency.GqlFieldImportGenerator;
import com.github.vladislavsevruk.generator.model.graphql.generator.dependency.GqlUnionImportGenerator;
import com.github.vladislavsevruk.generator.model.graphql.generator.field.GqlFieldAnnotationGenerator;
import com.github.vladislavsevruk.generator.model.graphql.generator.field.GqlUnionAnnotationGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

class GqlModelClassContentGeneratorProviderTest {

    @Test
    void getDefaultFieldAnnotationGeneratorsTest() {
        List<FieldAnnotationGenerator> generators
                = new GqlModelClassContentGeneratorProvider().getDefaultFieldAnnotationGenerators();
        Assertions.assertEquals(3, generators.size());
        Iterator<FieldAnnotationGenerator> iterator = generators.iterator();
        Assertions.assertEquals(JacksonFieldAnnotationGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(GqlFieldAnnotationGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(GqlUnionAnnotationGenerator.class, iterator.next().getClass());
    }

    @Test
    void getDefaultImportGeneratorsTest() {
        List<ClassElementCollectionGenerator> generators
                = new GqlModelClassContentGeneratorProvider().getDefaultImportGenerators();
        Assertions.assertEquals(8, generators.size());
        Iterator<ClassElementCollectionGenerator> iterator = generators.iterator();
        Assertions.assertEquals(JacksonImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(SuperclassImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(InterfaceImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(FieldTypeImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(LombokImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(EqualsMethodImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(GqlFieldImportGenerator.class, iterator.next().getClass());
        Assertions.assertEquals(GqlUnionImportGenerator.class, iterator.next().getClass());
    }
}
