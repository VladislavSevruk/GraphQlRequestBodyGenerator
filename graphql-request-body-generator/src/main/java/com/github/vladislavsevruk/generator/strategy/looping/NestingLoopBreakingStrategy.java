package com.github.vladislavsevruk.generator.strategy.looping;

import com.github.vladislavsevruk.resolver.type.TypeMeta;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Provides loop breaking strategy that allows to generate looped items with allowed level of nesting starting from 1
 * to keep only single item at query, e.g. for self-referencing structure <pre>item{id item{...}}</pre>
 * nesting level 0 (nesting disabled) -
 * <pre>
 * {@code
 * {
 *   id
 * }
 * </pre>
 * nesting level 1 -
 * <pre>
 * {@code
 * {
 *   id
 *   item {
 *     id
 *   }
 * }
 * }
 * </pre>
 * nesting level 2 -
 * <pre>
 * {@code
 *  {
 *    id
 *    item {
 *      id
 *      item {
 *        id
 *      }
 *   }
 * }
 * }
 * </pre>
 */
@Log4j2
public class NestingLoopBreakingStrategy implements LoopBreakingStrategy {

    private final int nestingLevel;

    public NestingLoopBreakingStrategy(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        if (nestingLevel < 0) {
            log.warn("Received nesting level at '{}' is '{}' while expected to be greater than or equal to zero.",
                    this.getClass().getName(), nestingLevel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShouldBreakOnItem(TypeMeta<?> typeMeta, List<TypeMeta<?>> trace) {
        return trace.stream().filter(traceItem -> traceItem.equals(typeMeta)).count() > nestingLevel + 1;
    }
}
