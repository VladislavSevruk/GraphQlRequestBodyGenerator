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

import com.github.vladislavsevruk.generator.model.graphql.constant.ElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlFloatType;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlIntType;
import org.gradle.api.provider.Property;

/**
 * Plugin extension with supported configuration parameters.
 */
public interface GqlModelGeneratorPluginExtension {

    /**
     * Returns <code>Property</code> with <code>Boolean</code> flag that reflects if jackson annotations should be
     * present at generated POJO models.
     */
    Property<Boolean> getAddJacksonAnnotations();

    /**
     * Returns <code>Property</code> with <code>String</code> postfix that should be added to generated POJO models.
     */
    Property<String> getEntitiesPostfix();

    /**
     * Returns <code>Property</code> with <code>String</code> prefix that should be added to generated POJO models.
     */
    Property<String> getEntitiesPrefix();

    /**
     * Returns <code>Property</code> with <code>String</code> path to GraphQL schema file.
     */
    Property<String> getPathToSchemaFile();

    /**
     * Returns <code>Property</code> with <code>String</code> java package of generated POJO models.
     */
    Property<String> getTargetPackage();

    /**
     * Returns <code>Property</code> with <code>ElementSequence</code> that should be used for GraphQL array
     * representation.
     */
    Property<ElementSequence> getTreatArrayAs();

    /**
     * Returns <code>Property</code> with <code>ElementSequence</code> that should be used for GraphQL Float type
     * representation.
     */
    Property<GqlFloatType> getTreatFloatAs();

    /**
     * Returns <code>Property</code> with <code>GqlIntType</code> that should be used for GraphQL ID type
     * representation.
     */
    Property<GqlIntType> getTreatIdAs();

    /**
     * Returns <code>Property</code> with <code>GqlIntType</code> that should be used for GraphQL Int type
     * representation.
     */
    Property<GqlIntType> getTreatIntAs();

    /**
     * Returns <code>Property</code> with <code>Boolean</code> flag that reflects if GraphQL schema element names should
     * be modified to follow camel case naming convention.
     */
    Property<Boolean> getUpdateNamesToJavaStyle();

    /**
     * Returns <code>Property</code> with <code>Boolean</code> flag that reflects if lombok annotations should be
     * present at generated POJO models instead of self-generated getters, setters, equals, hashCode and toString
     * methods.
     */
    Property<Boolean> getUseLombokAnnotations();

    /**
     * Returns <code>Property</code> with <code>Boolean</code> flag that reflects if java primitive types should be used
     * instead of corresponded wrapper types.
     */
    Property<Boolean> getUsePrimitivesInsteadOfWrappers();

    /**
     * Returns <code>Property</code> with <code>Boolean</code> flag that reflects if java String type should be used
     * instead of enum types.
     */
    Property<Boolean> getUseStringsInsteadOfEnums();
}
