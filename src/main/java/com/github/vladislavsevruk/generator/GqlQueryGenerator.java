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
package com.github.vladislavsevruk.generator;

import com.github.vladislavsevruk.generator.generator.GqlQueryBodyGenerator;
import com.github.vladislavsevruk.generator.param.QueryArgument;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategyManager;
import com.github.vladislavsevruk.generator.strategy.picker.AllFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.FieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyIdFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.OnlyNonNullableFieldsPickingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.WithoutFieldsWithSelectionSetPickingStrategy;
import com.github.vladislavsevruk.resolver.type.TypeProvider;

import java.util.Arrays;

/**
 * Generates bodies for GraphQL queries using received POJOs.<br> There are several predefined field picking strategies
 * for query body generation:<ul>
 * <li> pick all marked fields
 * <li> pick only fields that has name 'id' or fields that have nested 'id' field
 * <li> pick only fields that has 'non-nullable' flag
 * <li> pick all fields except fields with selection set
 * </ul>
 * Besides predefined ones custom field picking strategies can be provided for query body generation.<br> Fields marking
 * strategies are managed by {@link FieldMarkingStrategyManager}
 *
 * @see FieldMarkingStrategyManager
 */
public class GqlQueryGenerator {

    private GqlQueryGenerator() {
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(Class<?> clazz, QueryArgument<?>... queryArguments) {
        return allFields(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return allFields(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(TypeProvider<?> typeProvider, QueryArgument<?>... queryArguments) {
        return allFields(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(TypeProvider<?> typeProvider, Iterable<QueryArgument<?>> queryArguments) {
        return allFields(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(String queryName, Class<?> clazz, QueryArgument<?>... queryArguments) {
        return customQuery(queryName, clazz, new AllFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(String queryName, Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, clazz, new AllFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(String queryName, TypeProvider<?> typeProvider, QueryArgument<?>... queryArguments) {
        return customQuery(queryName, typeProvider, new AllFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String allFields(String queryName, TypeProvider<?> typeProvider,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, typeProvider, new AllFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(Class<?> clazz, FieldsPickingStrategy strategy,
            QueryArgument<?>... queryArguments) {
        return customQuery(null, clazz, strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(Class<?> clazz, FieldsPickingStrategy strategy,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(null, clazz, strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
            QueryArgument<?>... queryArguments) {
        return customQuery(null, typeProvider, strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(null, typeProvider, strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(String queryName, Class<?> clazz, FieldsPickingStrategy strategy,
            QueryArgument<?>... queryArguments) {
        return customQuery(queryName, clazz, strategy, Arrays.asList(queryArguments));
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(String queryName, Class<?> clazz, FieldsPickingStrategy strategy,
            Iterable<QueryArgument<?>> queryArguments) {
        return getQueryBodyBuilder(queryName, clazz).generate(strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(String queryName, TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
            QueryArgument<?>... queryArguments) {
        return customQuery(queryName, typeProvider, strategy, Arrays.asList(queryArguments));
    }

    /**
     * Generates GraphQL query body according to received fields picking strategy and current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param strategy       <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String customQuery(String queryName, TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
            Iterable<QueryArgument<?>> queryArguments) {
        return getQueryBodyBuilder(queryName, typeProvider).generate(strategy, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(Class<?> clazz, QueryArgument<?>... queryArguments) {
        return onlyId(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return onlyId(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(TypeProvider<?> typeProvider, QueryArgument<?>... queryArguments) {
        return onlyId(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(TypeProvider<?> typeProvider, Iterable<QueryArgument<?>> queryArguments) {
        return onlyId(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(String queryName, Class<?> clazz, QueryArgument<?>... queryArguments) {
        return customQuery(queryName, clazz, new OnlyIdFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(String queryName, Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, clazz, new OnlyIdFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(String queryName, TypeProvider<?> typeProvider, QueryArgument<?>... queryArguments) {
        return customQuery(queryName, typeProvider, new OnlyIdFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
     * according to current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyId(String queryName, TypeProvider<?> typeProvider,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, typeProvider, new OnlyIdFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(Class<?> clazz, QueryArgument<?>... queryArguments) {
        return onlyNonNullable(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return onlyNonNullable(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(TypeProvider<?> typeProvider, QueryArgument<?>... queryArguments) {
        return onlyNonNullable(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(TypeProvider<?> typeProvider, Iterable<QueryArgument<?>> queryArguments) {
        return onlyNonNullable(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(String queryName, Class<?> clazz, QueryArgument<?>... queryArguments) {
        return customQuery(queryName, clazz, new OnlyNonNullableFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(String queryName, Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, clazz, new OnlyNonNullableFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(String queryName, TypeProvider<?> typeProvider,
            QueryArgument<?>... queryArguments) {
        return customQuery(queryName, typeProvider, new OnlyNonNullableFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
     * FieldMarkingStrategy }.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String onlyNonNullable(String queryName, TypeProvider<?> typeProvider,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, typeProvider, new OnlyNonNullableFieldsPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(Class<?> clazz, QueryArgument<?>... queryArguments) {
        return withoutFieldsWithSelectionSet(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(Class<?> clazz, Iterable<QueryArgument<?>> queryArguments) {
        return withoutFieldsWithSelectionSet(null, clazz, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(TypeProvider<?> typeProvider,
            QueryArgument<?>... queryArguments) {
        return withoutFieldsWithSelectionSet(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(TypeProvider<?> typeProvider,
            Iterable<QueryArgument<?>> queryArguments) {
        return withoutFieldsWithSelectionSet(null, typeProvider, queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(String queryName, Class<?> clazz,
            QueryArgument<?>... queryArguments) {
        return customQuery(queryName, clazz, new WithoutFieldsWithSelectionSetPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param clazz          <code>Class</code> of POJO to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(String queryName, Class<?> clazz,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, clazz, new WithoutFieldsWithSelectionSetPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>QueryArgument</code> vararg with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(String queryName, TypeProvider<?> typeProvider,
            QueryArgument<?>... queryArguments) {
        return customQuery(queryName, typeProvider, new WithoutFieldsWithSelectionSetPickingStrategy(), queryArguments);
    }

    /**
     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
     * current {@link FieldMarkingStrategy}.
     *
     * @param queryName      <code>String</code> with custom query name.
     * @param typeProvider   <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
     * @param queryArguments <code>Iterable</code> of <code>QueryArgument</code> with query argument names and values.
     * @return <code>String</code> with generated GraphQL query.
     */
    public static String withoutFieldsWithSelectionSet(String queryName, TypeProvider<?> typeProvider,
            Iterable<QueryArgument<?>> queryArguments) {
        return customQuery(queryName, typeProvider, new WithoutFieldsWithSelectionSetPickingStrategy(), queryArguments);
    }

    private static GqlQueryBodyGenerator getQueryBodyBuilder(String queryName, Class<?> clazz) {
        return new GqlQueryBodyGenerator(queryName, clazz, FieldMarkingStrategyManager.getStrategy());
    }

    private static GqlQueryBodyGenerator getQueryBodyBuilder(String queryName, TypeProvider<?> typeProvider) {
        return new GqlQueryBodyGenerator(queryName, typeProvider, FieldMarkingStrategyManager.getStrategy());
    }
}
