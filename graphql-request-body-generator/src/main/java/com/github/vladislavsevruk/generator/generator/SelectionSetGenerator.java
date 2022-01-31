/*
 * MIT License
 *
 * Copyright (c) 2020 Uladzislau Seuruk
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
package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlUnion;
import com.github.vladislavsevruk.generator.annotation.GqlUnionType;
import com.github.vladislavsevruk.generator.strategy.looping.LoopBreakingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.selection.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeMetaResolver;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeResolver;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Generates selection sets for GraphQL operations for received model according to different field picking strategies.
 */
@Log4j2
public class SelectionSetGenerator {

    private static final String DELIMITER = " ";
    private final FieldMarkingStrategy fieldMarkingStrategy;
    private final LoopDetector loopDetector;
    private final TypeMeta<?> modelTypeMeta;
    private final ResolvingContext<TypeMeta<?>> resolvingContext = TypeMetaResolvingContextManager.getContext();
    private final FieldTypeResolver<TypeMeta<?>> fieldTypeResolver = new FieldTypeMetaResolver(resolvingContext);

    public SelectionSetGenerator(TypeMeta<?> modelTypeMeta, FieldMarkingStrategy fieldMarkingStrategy,
            LoopBreakingStrategy loopBreakingStrategy) {
        Objects.requireNonNull(modelTypeMeta);
        Objects.requireNonNull(fieldMarkingStrategy);
        this.modelTypeMeta = modelTypeMeta;
        this.fieldMarkingStrategy = fieldMarkingStrategy;
        this.loopDetector = new LoopDetector(modelTypeMeta, loopBreakingStrategy);
    }

    /**
     * Builds selection set for GraphQL operation according to received field picking strategy.
     *
     * @param fieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @return <code>String</code> with resulted selection set.
     */
    public String generate(FieldsPickingStrategy fieldsPickingStrategy) {
        Objects.requireNonNull(fieldsPickingStrategy);
        log.debug(() -> String.format("Generating selection set for '%s' model using '%s' field marking strategy "
                        + "and '%s' field picking strategy.", modelTypeMeta.getType().getName(),
                fieldMarkingStrategy.getClass().getName(), fieldsPickingStrategy.getClass().getName()));
        MappedVariableHierarchy<TypeMeta<?>> hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                .get(modelTypeMeta);
        Set<String> queryParams = collectQueryParameters(hierarchy, modelTypeMeta, fieldsPickingStrategy);
        return "{" + String.join(DELIMITER, queryParams) + "}";
    }

    private void addFieldWithSelectionSetQueryParameter(Set<String> queryParams, TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeMeta, field);
        loopDetector.addToTrace(fieldTypeMeta);
        if (loopDetector.isShouldBreakOnItem(fieldTypeMeta)) {
            log.warn(() -> String
                    .format("'%s' won't be added to selection set to avoid endless loop.", loopDetector.getTrace()));
        } else {
            Set<String> fieldWithSelectionSetQueryParams = collectFieldWithSelectionSetQueryParameters(fieldTypeMeta,
                    fieldsPickingStrategy);
            if (!fieldWithSelectionSetQueryParams.isEmpty()) {
                String fieldNameWithArgumentsAndAlias = GqlNamePicker.getFieldNameWithArgumentsAndAlias(field);
                String fieldWithSelectionSetQueryParam = fieldNameWithArgumentsAndAlias + "{" + String
                        .join(DELIMITER, fieldWithSelectionSetQueryParams) + "}";
                queryParams.add(fieldWithSelectionSetQueryParam);
            }
        }
        loopDetector.removeLastItemFromTrace();
    }

    private void addQueryParameter(Set<String> queryParams, TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        if (field.getAnnotation(GqlDelegate.class) != null) {
            log.debug(() -> String.format("'%s' is delegate.", field.getName()));
            queryParams.addAll(collectDelegatedQueryParameters(typeMeta, field, fieldsPickingStrategy));
        } else if (field.getAnnotation(GqlUnion.class) != null) {
            GqlUnion unionAnnotation = field.getAnnotation(GqlUnion.class);
            log.debug(() -> String.format("'%s' is union.", field.getName()));
            addUnionQueryParameters(queryParams, unionAnnotation.value(), field, fieldsPickingStrategy);
        } else {
            GqlField fieldAnnotation = field.getAnnotation(GqlField.class);
            if (fieldAnnotation != null && fieldAnnotation.withSelectionSet()) {
                log.debug(() -> String.format("'%s' is field with selection set.", field.getName()));
                addFieldWithSelectionSetQueryParameter(queryParams, typeMeta, field, fieldsPickingStrategy);
            } else {
                log.debug(() -> String.format("'%s' is field.", field.getName()));
                String fieldNameWithArgumentsAndAlias = GqlNamePicker.getFieldNameWithArgumentsAndAlias(field);
                queryParams.add(fieldNameWithArgumentsAndAlias);
            }
        }
    }

    private void addUnionQueryParameter(Set<String> queryParams, GqlUnionType unionType,
            FieldsPickingStrategy fieldsPickingStrategy) {
        TypeMeta<?> unionTypeMeta = new TypeMeta<>(unionType.value());
        loopDetector.addToTrace(unionTypeMeta);
        if (loopDetector.isShouldBreakOnItem(unionTypeMeta)) {
            log.warn(() -> String
                    .format("'%s' won't be added to selection set to avoid endless loop.", loopDetector.getTrace()));
        } else {
            MappedVariableHierarchy<TypeMeta<?>> hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                    .get(unionTypeMeta);
            Set<String> fieldWithSelectionSetQueryParams = collectQueryParameters(hierarchy, unionTypeMeta,
                    fieldsPickingStrategy);
            if (!fieldWithSelectionSetQueryParams.isEmpty()) {
                String unionName = GqlNamePicker.getUnionName(unionType);
                String fieldWithSelectionSetQueryParam = "... on " + unionName + "{" + String
                        .join(DELIMITER, fieldWithSelectionSetQueryParams) + "}";
                queryParams.add(fieldWithSelectionSetQueryParam);
            }
        }
        loopDetector.removeLastItemFromTrace();
    }

    private void addUnionQueryParameters(Set<String> queryParams, GqlUnionType[] unionTypes, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        Set<String> unionQueryParams = new LinkedHashSet<>();
        for (GqlUnionType unionType : unionTypes) {
            addUnionQueryParameter(unionQueryParams, unionType, fieldsPickingStrategy);
        }
        if (!unionQueryParams.isEmpty()) {
            String fieldNameWithArgumentsAndAlias = GqlNamePicker.getFieldNameWithArgumentsAndAlias(field);
            String fieldWithSelectionSetQueryParam = fieldNameWithArgumentsAndAlias + "{" + String
                    .join(DELIMITER, unionQueryParams) + "}";
            queryParams.add(fieldWithSelectionSetQueryParam);
        }
    }

    private Set<String> collectDelegatedQueryParameters(TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeMeta, field);
        MappedVariableHierarchy<TypeMeta<?>> hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                .get(fieldTypeMeta);
        return collectQueryParameters(hierarchy, fieldTypeMeta, fieldsPickingStrategy);
    }

    private Set<String> collectFieldWithSelectionSetQueryParameters(TypeMeta<?> fieldTypeMeta,
            FieldsPickingStrategy fieldsPickingStrategy) {
        if (Collection.class.isAssignableFrom(fieldTypeMeta.getType()) || fieldTypeMeta.getType().isArray()) {
            TypeMeta<?> genericTypeMeta = fieldTypeMeta.getGenericTypes()[0];
            MappedVariableHierarchy<TypeMeta<?>> hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                    .get(genericTypeMeta);
            return collectQueryParameters(hierarchy, genericTypeMeta, fieldsPickingStrategy);
        }
        MappedVariableHierarchy<TypeMeta<?>> hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                .get(fieldTypeMeta);
        return collectQueryParameters(hierarchy, fieldTypeMeta, fieldsPickingStrategy);
    }

    private Set<String> collectQueryParameters(MappedVariableHierarchy<TypeMeta<?>> hierarchy, TypeMeta<?> typeMeta,
            FieldsPickingStrategy fieldsPickingStrategy) {
        log.debug(() -> String.format("Collecting GraphQL fields for '%s' model.", typeMeta.getType().getName()));
        Set<String> queryParams = new LinkedHashSet<>();
        for (Field field : typeMeta.getType().getDeclaredFields()) {
            if (field.isSynthetic() || !fieldMarkingStrategy.isMarkedField(field)) {
                continue;
            }
            log.debug(() -> String.format("Marked '%s' selection set field.", field.getName()));
            if (fieldsPickingStrategy.shouldBePicked(field)) {
                log.debug(() -> String.format("Picked '%s' selection set field.", field.getName()));
                addQueryParameter(queryParams, typeMeta, field, fieldsPickingStrategy);
            }
        }
        Class<?> superclass = typeMeta.getType().getSuperclass();
        if (superclass != null && !Object.class.equals(superclass)) {
            queryParams.addAll(collectQueryParameters(hierarchy, getTypeMeta(hierarchy, superclass),
                    fieldsPickingStrategy));
        }
        return queryParams;
    }

    private TypeMeta<?> getTypeMeta(MappedVariableHierarchy<TypeMeta<?>> hierarchy, Class<?> clazz) {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = hierarchy.getTypeVariableMap(clazz);
        return resolvingContext.getTypeResolverPicker().pickTypeResolver(clazz).resolve(typeVariableMap, clazz);
    }
}
