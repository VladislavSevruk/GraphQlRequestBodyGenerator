package com.github.vladislavsevruk.generator.strategy.looping;

import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.util.List;

/**
 * Provides loop breaking strategy for picking element on which loop should be broken.
 */
@FunctionalInterface
public interface LoopBreakingStrategy {

    /**
     * Picks item from trace on which loop should be broken.
     *
     * @param trace <code>List</code> of <code>TypeMeta</code> with generation items trace.
     * @return <code>TypeMeta</code> on which loop should be broken.
     */
    @SuppressWarnings("java:S1452")
    TypeMeta<?> pickLoopBreakingItem(List<TypeMeta<?>> trace);
}
