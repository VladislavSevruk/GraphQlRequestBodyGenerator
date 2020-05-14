/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2020 Uladzislau Seuruk
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */
package com.github.vladislavsevruk.generator.generator.query;

import com.github.vladislavsevruk.generator.generator.GqlOperationGenerator;
import com.github.vladislavsevruk.generator.strategy.marker.FieldMarkingStrategySourceManager;

/**
 * Generates bodies for GraphQL queries using received POJOs.<br> There are several predefined field picking strategies
 * for query body generation:<ul>
 * <li> pick all marked fields
 * <li> pick only fields that has name 'id' or fields that have nested 'id' field
 * <li> pick only fields that has 'non-nullable' flag
 * <li> pick all fields except fields with selection set
 * </ul>
 * Besides predefined ones custom field picking strategies can be provided for query body generation.<br> Fields marking
 * strategies are managed by {@link FieldMarkingStrategySourceManager}
 *
 * @see FieldMarkingStrategySourceManager
 */
public class GqlQueryGenerator extends GqlOperationGenerator<GqlQueryGenerator> {

    public GqlQueryGenerator(String queryName) {
        super(queryName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generate() {
        verifySelectionSet();
        return new GqlQueryBodyGenerator(getOperationName(), getSelectionSetGenerator())
                .generate(getSelectionSetFieldsPickingStrategy(), getArguments());
    }
}
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return allFields(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(Class<?> clazz, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return allFields(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(TypeProvider<?> typeProvider, GqlParameterValue<?>... arguments) {
//        return allFields(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(TypeProvider<?> typeProvider, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return allFields(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(String queryName, Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, clazz, new AllFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(String queryName, Class<?> clazz,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, clazz, new AllFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(String queryName, TypeProvider<?> typeProvider, GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, typeProvider, new AllFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String allFields(String queryName, TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, typeProvider, new AllFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param strategy  <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(Class<?> clazz, FieldsPickingStrategy strategy,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(null, clazz, strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param strategy  <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(Class<?> clazz, FieldsPickingStrategy strategy,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(null, clazz, strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param strategy     <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(null, typeProvider, strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param strategy     <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(null, typeProvider, strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param strategy  <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(String queryName, Class<?> clazz, FieldsPickingStrategy strategy,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, clazz, strategy, Arrays.asList(arguments));
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param strategy  <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(String queryName, Class<?> clazz, FieldsPickingStrategy strategy,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return getQueryBodyBuilder(queryName, clazz).generate(strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param strategy     <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(String queryName, TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, typeProvider, strategy, Arrays.asList(arguments));
//    }
//
//    /**
//     * Generates GraphQL query body according to received fields picking strategy and current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param strategy     <code>FieldsPickingStrategy</code> with custom strategy for fields picking.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String customQuery(String queryName, TypeProvider<?> typeProvider, FieldsPickingStrategy strategy,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return getQueryBodyBuilder(queryName, typeProvider).generate(strategy, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return onlyId(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(Class<?> clazz, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return onlyId(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(TypeProvider<?> typeProvider, GqlParameterValue<?>... arguments) {
//        return onlyId(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(TypeProvider<?> typeProvider, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return onlyId(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(String queryName, Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, clazz, new OnlyIdFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(String queryName, Class<?> clazz, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, clazz, new OnlyIdFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(String queryName, TypeProvider<?> typeProvider, GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, typeProvider, new OnlyIdFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that has name 'id' or fields that have nested field with name 'id'
//     * according to current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyId(String queryName, TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, typeProvider, new OnlyIdFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return onlyNonNullable(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(Class<?> clazz, Iterable<? extends GqlParameterValue<?>> arguments) {
//        return onlyNonNullable(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(TypeProvider<?> typeProvider, GqlParameterValue<?>... arguments) {
//        return onlyNonNullable(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return onlyNonNullable(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(String queryName, Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, clazz, new OnlyNonNullFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(String queryName, Class<?> clazz,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, clazz, new OnlyNonNullFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(String queryName, TypeProvider<?> typeProvider,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, typeProvider, new OnlyNonNullFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with only fields that was marked as non-nullable and according to current {@link
//     * FieldMarkingStrategy }.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String onlyNonNullable(String queryName, TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, typeProvider, new OnlyNonNullFieldsPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(Class<?> clazz, GqlParameterValue<?>... arguments) {
//        return withoutFieldsWithSelectionSet(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(Class<?> clazz,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return withoutFieldsWithSelectionSet(null, clazz, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(TypeProvider<?> typeProvider,
//            GqlParameterValue<?>... arguments) {
//        return withoutFieldsWithSelectionSet(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return withoutFieldsWithSelectionSet(null, typeProvider, arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(String queryName, Class<?> clazz,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, clazz, new WithoutFieldsWithSelectionSetPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName <code>String</code> with custom query name.
//     * @param clazz     <code>Class</code> of POJO to generate GraphQL query body for.
//     * @param arguments <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(String queryName, Class<?> clazz,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, clazz, new WithoutFieldsWithSelectionSetPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>GqlParameterValue</code> varargs with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(String queryName, TypeProvider<?> typeProvider,
//            GqlParameterValue<?>... arguments) {
//        return customQuery(queryName, typeProvider, new WithoutFieldsWithSelectionSetPickingStrategy(), arguments);
//    }
//
//    /**
//     * Generates GraphQL query body with all fields except ones that have selection set with nested fields according to
//     * current {@link FieldMarkingStrategy}.
//     *
//     * @param queryName    <code>String</code> with custom query name.
//     * @param typeProvider <code>TypeProvider</code> with POJO class to generate GraphQL query body for.
//     * @param arguments    <code>Iterable</code> of <code>GqlParameterValue</code> with argument names and values.
//     * @return <code>String</code> with generated GraphQL query.
//     */
//    public static String withoutFieldsWithSelectionSet(String queryName, TypeProvider<?> typeProvider,
//            Iterable<? extends GqlParameterValue<?>> arguments) {
//        return customQuery(queryName, typeProvider, new WithoutFieldsWithSelectionSetPickingStrategy(), arguments);
//    }
//
//    private static GqlQueryBodyGenerator getQueryBodyBuilder(String queryName, Class<?> clazz) {
//        return new GqlQueryBodyGenerator(queryName, clazz,
//                FieldMarkingStrategySourceManager.selectionSet().getStrategy());
//    }
//
//    private static GqlQueryBodyGenerator getQueryBodyBuilder(String queryName, TypeProvider<?> typeProvider) {
//        return new GqlQueryBodyGenerator(queryName, typeProvider,
//                FieldMarkingStrategySourceManager.selectionSet().getStrategy());
//    }
//}
