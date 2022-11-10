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
package com.github.vladislavsevruk.generator.model.graphql.extension;

import lombok.extern.slf4j.Slf4j;
import org.gradle.api.provider.Property;

import java.lang.reflect.Method;

/**
 * Builds string representation of configuration values.
 */
@Slf4j
public class PluginExtensionValueStringBuilder {

    /**
     * Builds string representation of configuration values.
     *
     * @param pluginExtension <code>Object</code> to get configuration values from.
     * @return <code>String</code> representation of configuration values.
     */
    public String build(Object pluginExtension) {
        StringBuilder builder = new StringBuilder();
        Method[] methods = pluginExtension.getClass().getMethods();
        boolean foundAny = false;
        for (Method method : methods) {
            if (isPropertyMethod(method)) {
                if (!foundAny) {
                    foundAny = true;
                } else {
                    builder.append(",");
                }
                try {
                    Object value = ((Property<?>) method.invoke(pluginExtension)).get();
                    builder.append(method.getName()).append(":").append(value);
                } catch (ReflectiveOperationException roEx) {
                    log.warn("Issue happened during configuration check", roEx);
                }
            }
        }
        return builder.toString();
    }

    private boolean isPropertyMethod(Method method) {
        return Property.class.equals(method.getReturnType()) && method.getParameters().length == 0;
    }
}
