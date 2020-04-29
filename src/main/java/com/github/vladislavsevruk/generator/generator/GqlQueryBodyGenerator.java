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
import com.github.vladislavsevruk.generator.annotation.GqlEntity;
import com.github.vladislavsevruk.generator.param.QueryArgument;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.util.GqlNamePicker;
import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.ResolvingContextManager;
import com.github.vladislavsevruk.resolver.resolver.FieldTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.FieldTypeResolverImpl;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Generates body for GraphQL queries for received model according to different field picking strategies.
 */
public class GqlQueryBodyGenerator {

    private static final String DELIMITER = " ";
    private final FieldMarkingStrategy fieldMarkingStrategy;
    private final TypeMeta<?> modelTypeMeta;
    private final String queryName;
    private final ResolvingContext resolvingContext = ResolvingContextManager.getContext();
    private final FieldTypeResolver fieldTypeResolver = new FieldTypeResolverImpl(resolvingContext);

    public GqlQueryBodyGenerator(String queryName, TypeProvider<?> typeProvider,
            FieldMarkingStrategy fieldMarkingStrategy) {
        this(queryName, typeProvider.getTypeMeta(), fieldMarkingStrategy);
    }

    public GqlQueryBodyGenerator(String queryName, Class<?> model, FieldMarkingStrategy fieldMarkingStrategy) {
        this(queryName, new TypeMeta<>(model), fieldMarkingStrategy);
    }

    private GqlQueryBodyGenerator(String queryName, TypeMeta<?> modelTypeMeta,
            FieldMarkingStrategy fieldMarkingStrategy) {
        Objects.requireNonNull(modelTypeMeta);
        Objects.requireNonNull(fieldMarkingStrategy);
        this.modelTypeMeta = modelTypeMeta;
        this.queryName = queryName != null ? queryName : GqlNamePicker.getQueryName(modelTypeMeta.getType());
        this.fieldMarkingStrategy = fieldMarkingStrategy;
    }

    /**
     * Builds GraphQL query body with received query arguments according to received field picking strategy.
     *
     * @param fieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param queryArguments        <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String build(FieldsPickingStrategy fieldsPickingStrategy, QueryArgument<?>... queryArguments) {
        return build(fieldsPickingStrategy, Arrays.asList(queryArguments));
    }

    /**
     * Builds GraphQL query body with received query arguments according to received field picking strategy.
     *
     * @param fieldsPickingStrategy <code>FieldsPickingStrategy</code> to filter required fields for query.
     * @param queryArguments        <code>Iterable</code> of <code>QueryArgument</code> with query argument names and
     *                              values.
     * @return <code>String</code> with resulted GraphQL query.
     */
    public String build(FieldsPickingStrategy fieldsPickingStrategy, Iterable<QueryArgument<?>> queryArguments) {
        Objects.requireNonNull(fieldsPickingStrategy);
        Objects.requireNonNull(queryArguments);
        MappedVariableHierarchy hierarchy = resolvingContext.getMappedVariableHierarchyStorage().get(modelTypeMeta);
        Set<String> queryParams = collectQueryParameters(hierarchy, modelTypeMeta, fieldsPickingStrategy);
        return "{\"query\":\"{" + queryName + generateQueryArguments(queryArguments) + "{" + String
                .join(DELIMITER, queryParams) + "}}\"}";
    }

    private void addEntityQueryParameter(Set<String> queryParams, TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeMeta, field);
        Set<String> nestedQueryParams = collectNestedQueryParameters(fieldTypeMeta, fieldsPickingStrategy);
        if (!nestedQueryParams.isEmpty()) {
            String entityQueryParam = GqlNamePicker.getFieldName(field) + "{" + String
                    .join(DELIMITER, nestedQueryParams) + "}";
            queryParams.add(entityQueryParam);
        }
    }

    private void addQueryParameter(Set<String> queryParams, TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        if (field.getAnnotation(GqlDelegate.class) != null) {
            queryParams.addAll(collectDelegatedQueryParameters(typeMeta, field, fieldsPickingStrategy));
        } else if (field.getAnnotation(GqlEntity.class) != null) {
            addEntityQueryParameter(queryParams, typeMeta, field, fieldsPickingStrategy);
        } else {
            queryParams.add(GqlNamePicker.getFieldName(field));
        }
    }

    private Set<String> collectDelegatedQueryParameters(TypeMeta<?> typeMeta, Field field,
            FieldsPickingStrategy fieldsPickingStrategy) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeMeta, field);
        MappedVariableHierarchy hierarchy = resolvingContext.getMappedVariableHierarchyStorage().get(fieldTypeMeta);
        return collectQueryParameters(hierarchy, fieldTypeMeta, fieldsPickingStrategy);
    }

    private Set<String> collectNestedQueryParameters(TypeMeta<?> fieldTypeMeta,
            FieldsPickingStrategy fieldsPickingStrategy) {
        if (Collection.class.isAssignableFrom(fieldTypeMeta.getType())) {
            TypeMeta<?> genericTypeMeta = fieldTypeMeta.getGenericTypes()[0];
            MappedVariableHierarchy hierarchy = resolvingContext.getMappedVariableHierarchyStorage()
                    .get(genericTypeMeta);
            return collectQueryParameters(hierarchy, genericTypeMeta, fieldsPickingStrategy);
        }
        MappedVariableHierarchy hierarchy = resolvingContext.getMappedVariableHierarchyStorage().get(fieldTypeMeta);
        return collectQueryParameters(hierarchy, fieldTypeMeta, fieldsPickingStrategy);
    }

    private Set<String> collectQueryParameters(MappedVariableHierarchy hierarchy, TypeMeta<?> typeMeta,
            FieldsPickingStrategy fieldsPickingStrategy) {
        Set<String> queryParams = new LinkedHashSet<>();
        for (Field field : typeMeta.getType().getDeclaredFields()) {
            if (field.isSynthetic() || !fieldMarkingStrategy.isMarkedField(field)) {
                continue;
            }
            if (fieldsPickingStrategy.shouldBePicked(field)) {
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

    private List<String> convertToStringList(Object value) {
        return createStream(value).map(Object::toString).collect(Collectors.toList());
    }

    private Stream<?> createStream(Object value) {
        if (Iterable.class.isAssignableFrom(value.getClass())) {
            return StreamSupport.stream(((Iterable<?>) value).spliterator(), false);
        }
        return Arrays.stream((Object[]) value);
    }

    private String generateQueryArguments(Iterable<QueryArgument<?>> argumentValue) {
        if (!argumentValue.iterator().hasNext()) {
            return "";
        }
        return "(" + StreamSupport.stream(argumentValue.spliterator(), false).map(this::performArgumentModifications)
                .map(argument -> argument.getName() + ":" + argument.getValue()).collect(Collectors.joining(",")) + ")";
    }

    private TypeMeta<?> getTypeMeta(MappedVariableHierarchy hierarchy, Class<?> clazz) {
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(clazz);
        return resolvingContext.getTypeResolverPicker().pickTypeResolver(clazz).resolve(typeVariableMap, clazz);
    }

    /**
     * Performs modifications for arguments, e.g. - escapes quotes for literals: { "key", "literalValue" } -> { "key",
     * "\"literalValue\"" }, { "key", "literal"With"Quotes" } -> { "key", "\"literal\\\"With\\\"Quotes\"" } - compose
     * iterables or arrays to string: { "key", [ value1, value2 ] } -> { "key", "[value1,value2]" }
     */
    private QueryArgument<?> performArgumentModifications(QueryArgument<?> queryArgument) {
        Object value = queryArgument.getValue();
        if (value == null) {
            return queryArgument;
        }
        Class<?> valueClass = value.getClass();
        if (Iterable.class.isAssignableFrom(valueClass) || valueClass.isArray()) {
            // compose all elements through the comma and surround by square brackets
            String modifiedValue = "[" + String.join(",", convertToStringList(value)) + "]";
            return new QueryArgument<>(queryArgument.getName(), modifiedValue);
        }
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            // add escaped quotes for literals
            String modifiedValue = String.format("\\\"%s\\\"", value.toString().replace("\"", "\\\\\\\""));
            return new QueryArgument<>(queryArgument.getName(), modifiedValue);
        }
        return queryArgument;
    }
}
