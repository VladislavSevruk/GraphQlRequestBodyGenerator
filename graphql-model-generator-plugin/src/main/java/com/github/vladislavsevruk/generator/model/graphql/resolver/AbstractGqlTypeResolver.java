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
package com.github.vladislavsevruk.generator.model.graphql.resolver;

import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.CommonJavaSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.PrimitiveSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ArraySchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.CollectionSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.IterableSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.ListSchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.SetSchemaEntity;
import com.github.vladislavsevruk.generator.model.graphql.constant.ElementSequence;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlFloatType;
import com.github.vladislavsevruk.generator.model.graphql.constant.GqlIntType;
import com.github.vladislavsevruk.generator.model.graphql.extension.GqlModelGeneratorPluginExtension;
import com.github.vladislavsevruk.generator.model.graphql.type.GqlScalarType;

/**
 * Contains common logic for GraphQL type resolvers.
 */
public abstract class AbstractGqlTypeResolver implements GqlTypeResolver {

    protected final GqlModelGeneratorPluginExtension pluginExtension;

    protected AbstractGqlTypeResolver(GqlModelGeneratorPluginExtension pluginExtension) {
        this.pluginExtension = pluginExtension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemaEntity pickJavaType(SchemaEntity schemaEntity, boolean isArray) {
        schemaEntity = pickJavaType(schemaEntity);
        return isArray ? wrapForArray(schemaEntity) : schemaEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemaEntity pickJavaType(GqlScalarType scalarType, boolean isArray) {
        // always wrappers for array types
        boolean usePrimitives = !isArray && pluginExtension.getUsePrimitivesInsteadOfWrappers().get();
        SchemaEntity schemaEntity = pickJavaScalarType(scalarType, usePrimitives);
        return pickJavaType(schemaEntity, isArray);
    }

    protected abstract SchemaEntity pickJavaType(SchemaEntity schemaEntity);

    private IllegalArgumentException newNotImplementedException(Object type) {
        return new IllegalArgumentException("Not implemented for type: " + type);
    }

    private SchemaEntity pickBooleanType(boolean usePrimitives) {
        return usePrimitives ? PrimitiveSchemaEntity.BOOLEAN : CommonJavaSchemaEntity.BOOLEAN;
    }

    private SchemaEntity pickDoubleType(boolean usePrimitives) {
        return usePrimitives ? PrimitiveSchemaEntity.DOUBLE : CommonJavaSchemaEntity.DOUBLE;
    }

    private SchemaEntity pickFloatType(boolean usePrimitives) {
        return usePrimitives ? PrimitiveSchemaEntity.FLOAT : CommonJavaSchemaEntity.FLOAT;
    }

    private SchemaEntity pickGqlFloatType(boolean usePrimitives) {
        GqlFloatType gqlFloatType = pluginExtension.getTreatFloatAs().get();
        switch (gqlFloatType) {
            case BIG_DECIMAL:
                return CommonJavaSchemaEntity.BIG_DECIMAL;
            case DOUBLE:
                return pickDoubleType(usePrimitives);
            case FLOAT:
                return pickFloatType(usePrimitives);
            case STRING:
                return CommonJavaSchemaEntity.STRING;
            default:
                throw newNotImplementedException(gqlFloatType);
        }
    }

    private SchemaEntity pickGqlIntType(GqlIntType gqlIntType, boolean usePrimitives) {
        switch (gqlIntType) {
            case BIG_INTEGER:
                return CommonJavaSchemaEntity.BIG_INTEGER;
            case INTEGER:
                return pickIntegerType(usePrimitives);
            case LONG:
                return pickLongType(usePrimitives);
            case STRING:
                return CommonJavaSchemaEntity.STRING;
            default:
                throw newNotImplementedException(gqlIntType);
        }
    }

    private SchemaEntity pickGqlIntType(boolean usePrimitives) {
        return pickGqlIntType(pluginExtension.getTreatIntAs().get(), usePrimitives);
    }

    private SchemaEntity pickIdType(boolean usePrimitives) {
        return pickGqlIntType(pluginExtension.getTreatIdAs().get(), usePrimitives);
    }

    private SchemaEntity pickIntegerType(boolean usePrimitives) {
        return usePrimitives ? PrimitiveSchemaEntity.INT : CommonJavaSchemaEntity.INTEGER;
    }

    private SchemaEntity pickJavaScalarType(GqlScalarType scalarType, boolean usePrimitives) {
        switch (scalarType) {
            case BOOLEAN_TYPE:
                return pickBooleanType(usePrimitives);
            case INT_TYPE:
                return pickGqlIntType(usePrimitives);
            case ID_TYPE:
                return pickIdType(usePrimitives);
            case FLOAT_TYPE:
                return pickGqlFloatType(usePrimitives);
            case STRING_TYPE:
                return CommonJavaSchemaEntity.STRING;
            default:
                throw newNotImplementedException(scalarType);
        }
    }

    private SchemaEntity pickLongType(boolean usePrimitives) {
        return usePrimitives ? PrimitiveSchemaEntity.LONG : CommonJavaSchemaEntity.LONG;
    }

    private SchemaEntity wrapForArray(SchemaEntity schemaEntity) {
        ElementSequence elementSequence = pluginExtension.getTreatArrayAs().get();
        switch (elementSequence) {
            case ARRAY:
                return new ArraySchemaEntity(schemaEntity);
            case COLLECTION:
                return new CollectionSchemaEntity(schemaEntity);
            case ITERABLE:
                return new IterableSchemaEntity(schemaEntity);
            case LIST:
                return new ListSchemaEntity(schemaEntity);
            case SET:
                return new SetSchemaEntity(schemaEntity);
            default:
                throw newNotImplementedException(elementSequence);
        }
    }
}
