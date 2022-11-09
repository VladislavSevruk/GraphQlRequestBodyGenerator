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
package com.github.vladislavsevruk.generator.model.graphql.type;

import com.github.vladislavsevruk.generator.java.type.BaseSchemaField;
import com.github.vladislavsevruk.generator.java.type.JacksonSchemaField;
import com.github.vladislavsevruk.generator.java.type.SchemaEntity;
import com.github.vladislavsevruk.generator.java.type.predefined.sequence.BaseSchemaElementSequence;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.function.Supplier;

/**
 * Represents GraphQL field meta information.
 */
@Log4j2
public class GqlSchemaField extends BaseSchemaField implements JacksonSchemaField {

    @Getter
    private final boolean nonNull;
    @Getter
    private final String rawSchemaName;

    public GqlSchemaField(String name, String rawSchemaName, SchemaEntity type, boolean nonNull) {
        super(name, type);
        this.rawSchemaName = rawSchemaName;
        this.nonNull = nonNull;
    }

    public GqlSchemaField(String name, String rawSchemaName, Supplier<SchemaEntity> delayedElementTypeSupplier,
            boolean nonNull) {
        super(name, delayedElementTypeSupplier);
        this.rawSchemaName = rawSchemaName;
        this.nonNull = nonNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNameForJackson() {
        return rawSchemaName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public SchemaEntity getType() {
        if (isUnion()) {
            SchemaEntity type = super.getType();
            return isElementSequence(type) ? createElementSequence(
                    (Class<? extends BaseSchemaElementSequence>) type.getClass(),
                    ((BaseSchemaElementSequence) type).getElementTypes().iterator().next()) : type;
        }
        return super.getType();
    }

    public GqlSchemaUnionType[] getUnionTypes() {
        SchemaEntity targetType = getTargetType(super.getType());
        return isUnion(targetType) ? ((GqlSchemaUnion) targetType).getUnionTypes() : null;
    }

    public boolean isUnion() {
        SchemaEntity type = getTargetType(super.getType());
        return isUnion(type);
    }

    private SchemaEntity createElementSequence(Class<? extends BaseSchemaElementSequence> targetType,
            SchemaEntity innerType) {
        try {
            return targetType.getConstructor(SchemaEntity.class).newInstance(innerType);
        } catch (ReflectiveOperationException roEx) {
            log.warn(() -> String.format("Failed to create target element '%s' sequence schema.", targetType.getName()),
                    roEx);
            return null;
        }
    }

    private SchemaEntity getTargetType(SchemaEntity type) {
        if (isElementSequence(type)) {
            return ((BaseSchemaElementSequence) type).getElementTypes().iterator().next();
        }
        return type;
    }

    private boolean isElementSequence(SchemaEntity type) {
        return BaseSchemaElementSequence.class.isAssignableFrom(type.getClass());
    }

    private boolean isUnion(SchemaEntity type) {
        return GqlSchemaUnion.class.isAssignableFrom(type.getClass());
    }
}
