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
package com.github.vladislavsevruk.generator.model.graphql.test.data.extension;

import com.github.vladislavsevruk.generator.model.graphql.constant.ElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlFloatType;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlIntType;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import org.gradle.api.provider.Property;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public final class TestExtensionUtil {

    private TestExtensionUtil() {
    }

    public static GqlModelGeneratorPluginExtension mockPluginExtension() {
        GqlModelGeneratorPluginExtension pluginExtension = Mockito.mock(GqlModelGeneratorPluginExtension.class);
        Property<Boolean> addJacksonAnnotations = mockProperty(true);
        when(pluginExtension.getAddJacksonAnnotations()).thenReturn(addJacksonAnnotations);
        Property<String> entitiesPostfix = mockProperty("");
        when(pluginExtension.getEntitiesPostfix()).thenReturn(entitiesPostfix);
        Property<String> entitiesPrefix = mockProperty("");
        when(pluginExtension.getEntitiesPrefix()).thenReturn(entitiesPrefix);
        Property<String> pathToSchemaFile = mockProperty("");
        when(pluginExtension.getPathToSchemaFile()).thenReturn(pathToSchemaFile);
        Property<String> targetPackage = mockProperty("com.test");
        when(pluginExtension.getTargetPackage()).thenReturn(targetPackage);
        Property<ElementSequence> treatArrayAs = mockProperty(ElementSequence.ARRAY);
        when(pluginExtension.getTreatArrayAs()).thenReturn(treatArrayAs);
        Property<GqlFloatType> treatFloatAs = mockProperty(GqlFloatType.DOUBLE);
        when(pluginExtension.getTreatFloatAs()).thenReturn(treatFloatAs);
        Property<GqlIntType> treatIdAs = mockProperty(GqlIntType.INTEGER);
        when(pluginExtension.getTreatIdAs()).thenReturn(treatIdAs);
        Property<GqlIntType> treatIntAs = mockProperty(GqlIntType.INTEGER);
        when(pluginExtension.getTreatIntAs()).thenReturn(treatIntAs);
        Property<Boolean> updateNamesToJavaStyle = mockProperty(true);
        when(pluginExtension.getUpdateNamesToJavaStyle()).thenReturn(updateNamesToJavaStyle);
        Property<Boolean> useLombokAnnotations = mockProperty(true);
        when(pluginExtension.getUseLombokAnnotations()).thenReturn(useLombokAnnotations);
        Property<Boolean> usePrimitivesInsteadOfWrappers = mockProperty(true);
        when(pluginExtension.getUsePrimitivesInsteadOfWrappers()).thenReturn(usePrimitivesInsteadOfWrappers);
        Property<Boolean> useStringsInsteadOfEnums = mockProperty(true);
        when(pluginExtension.getUseStringsInsteadOfEnums()).thenReturn(useStringsInsteadOfEnums);
        return pluginExtension;
    }

    @SuppressWarnings("unchecked")
    public static <T> Property<T> mockProperty(T value) {
        Property<T> property = Mockito.mock(Property.class);
        when(property.get()).thenReturn(value);
        when(property.convention((T) any())).thenReturn(property);
        return property;
    }
}
