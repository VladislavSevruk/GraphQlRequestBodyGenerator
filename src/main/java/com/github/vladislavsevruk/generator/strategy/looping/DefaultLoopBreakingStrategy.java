package com.github.vladislavsevruk.generator.strategy.looping;

import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.util.List;

/**
 * Provides default loop breaking strategy for excluding looped element from looping sequence.
 */
public class DefaultLoopBreakingStrategy implements LoopBreakingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeMeta<?> pickLoopBreakingItem(List<TypeMeta<?>> trace) {
        return trace.get(trace.size() - 1);
    }
}
