package com.github.vladislavsevruk.generator.strategy.looping;

import lombok.Getter;

/**
 * Contains predefined {@link LoopBreakingStrategy} for selection set:<ul>
 * <li> break on first element of looping sequence
 * </ul>
 *
 * @see LoopBreakingStrategy
 * @see DefaultLoopBreakingStrategy
 */
public enum EndlessLoopBreakingStrategy {

    EXCLUDE_FIRST_ENTRY(new DefaultLoopBreakingStrategy());

    @Getter
    private LoopBreakingStrategy loopBreakingStrategy;

    EndlessLoopBreakingStrategy(LoopBreakingStrategy loopBreakingStrategy) {
        this.loopBreakingStrategy = loopBreakingStrategy;
    }

    /**
     * Returns default loop breaking strategy.
     */
    public static EndlessLoopBreakingStrategy defaultStrategy() {
        return excludeFirstEntry();
    }

    /**
     * Returns strategy for loop breaking excluding first looped element of looping sequence.
     */
    public static EndlessLoopBreakingStrategy excludeFirstEntry() {
        return EXCLUDE_FIRST_ENTRY;
    }
}
