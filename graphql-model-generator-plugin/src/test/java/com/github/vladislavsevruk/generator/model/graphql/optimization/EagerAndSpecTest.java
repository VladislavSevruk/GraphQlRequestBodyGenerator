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
package com.github.vladislavsevruk.generator.model.graphql.optimization;

import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class EagerAndSpecTest {

    @Test
    void isNotSatisfiedBySeveralSpecsTest() {
        Task task = Mockito.mock(Task.class);
        Spec<Task> spec1 = Mockito.mock(Spec.class);
        when(spec1.isSatisfiedBy(task)).thenReturn(true);
        Spec<Task> spec2 = Mockito.mock(Spec.class);
        when(spec2.isSatisfiedBy(task)).thenReturn(false);
        Spec<Task> spec3 = Mockito.mock(Spec.class);
        when(spec3.isSatisfiedBy(task)).thenReturn(true);
        EagerAndSpec<Task> eagerAndSpec = new EagerAndSpec<>(spec1, spec2, spec3);
        Assertions.assertFalse(eagerAndSpec.isSatisfiedBy(task));
        verify(spec1).isSatisfiedBy(task);
        verify(spec2).isSatisfiedBy(task);
        verify(spec3).isSatisfiedBy(task);
    }

    @Test
    void isNotSatisfiedBySingleSpecTest() {
        Task task = Mockito.mock(Task.class);
        Spec<Task> spec = Mockito.mock(Spec.class);
        when(spec.isSatisfiedBy(task)).thenReturn(false);
        EagerAndSpec<Task> eagerAndSpec = new EagerAndSpec<>(spec);
        Assertions.assertFalse(eagerAndSpec.isSatisfiedBy(task));
        verify(spec).isSatisfiedBy(task);
    }

    @Test
    void isSatisfiedBySeveralSpecsTest() {
        Task task = Mockito.mock(Task.class);
        Spec<Task> spec1 = Mockito.mock(Spec.class);
        when(spec1.isSatisfiedBy(task)).thenReturn(true);
        Spec<Task> spec2 = Mockito.mock(Spec.class);
        when(spec2.isSatisfiedBy(task)).thenReturn(true);
        EagerAndSpec<Task> eagerAndSpec = new EagerAndSpec<>(spec1, spec2);
        Assertions.assertTrue(eagerAndSpec.isSatisfiedBy(task));
        verify(spec1).isSatisfiedBy(task);
        verify(spec2).isSatisfiedBy(task);
    }

    @Test
    void isSatisfiedBySingleSpecTest() {
        Task task = Mockito.mock(Task.class);
        Spec<Task> spec = Mockito.mock(Spec.class);
        when(spec.isSatisfiedBy(task)).thenReturn(true);
        EagerAndSpec<Task> eagerAndSpec = new EagerAndSpec<>(spec);
        Assertions.assertTrue(eagerAndSpec.isSatisfiedBy(task));
        verify(spec).isSatisfiedBy(task);
    }
}
