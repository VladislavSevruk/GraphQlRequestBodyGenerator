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
package com.github.vladislavsevruk.generator;

import com.github.vladislavsevruk.generator.generator.mutation.UnwrappedGqlMutationRequestBodyGenerator;
import com.github.vladislavsevruk.generator.generator.query.UnwrappedGqlQueryRequestBodyGenerator;

/**
 * Provides generators for unwrapped GraphQL operations.
 */
public class UnwrappedGqlRequestBodyGenerator {

    private UnwrappedGqlRequestBodyGenerator() {
    }

    /**
     * Returns new instance of {@link UnwrappedGqlMutationRequestBodyGenerator} with received mutation name.
     *
     * @param mutationName <code>String</code> with name of mutation to generate.
     * @return <code>UnwrappedGqlMutationGenerator</code> with received mutation name.
     */
    public static UnwrappedGqlMutationRequestBodyGenerator mutation(String mutationName) {
        return new UnwrappedGqlMutationRequestBodyGenerator(mutationName);
    }

    /**
     * Returns new instance of {@link UnwrappedGqlQueryRequestBodyGenerator} with received query name.
     *
     * @param queryName <code>String</code> with name of query to generate.
     * @return <code>UnwrappedGqlMutationGenerator</code> with received mutation name.
     */
    public static UnwrappedGqlQueryRequestBodyGenerator query(String queryName) {
        return new UnwrappedGqlQueryRequestBodyGenerator(queryName);
    }
}
