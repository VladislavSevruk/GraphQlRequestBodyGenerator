package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.strategy.looping.LoopBreakingStrategy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Detects if POJOs was configured with circle reference on each other and helps to avoid endless looping.
 */
public final class LoopDetector {

    private boolean isLoopDetected;
    // ArrayList is permissible as we delete elements only from list end
    private final List<TypeMeta<?>> itemsToBreakOn = new ArrayList<>();
    private final LoopBreakingStrategy loopBreakingStrategy;
    private final List<TypeMeta<?>> loopedItems = new ArrayList<>();
    private final List<TypeMeta<?>> trace = new ArrayList<>();

    public LoopDetector(TypeMeta<?> initialTypeMeta, LoopBreakingStrategy loopBreakingStrategy) {
        this.loopBreakingStrategy = loopBreakingStrategy;
        trace.add(initialTypeMeta);
    }

    /**
     * Adds received type meta as trace element and checks if such element was already present at trace.
     *
     * @param typeMeta new <code>TypeMeta</code> trace element.
     */
    public void addToTrace(TypeMeta<?> typeMeta) {
        trace.add(typeMeta);
        checkForLooping(typeMeta);
    }

    /**
     * Returns trace elements as <code>String</code> with class names separated by dot ('.').
     */
    public String getTrace() {
        return trace.stream().map(TypeMeta::getType).map(Class::getSimpleName).collect(Collectors.joining("."));
    }

    /**
     * Removes last item from trace and checks if elements looping is still present at trace.
     */
    public void removeLastItemFromTrace() {
        TypeMeta<?> lastItem = trace.remove(trace.size() - 1);
        if (!loopedItems.isEmpty()) {
            int lastLoopedItemIndex = loopedItems.size() - 1;
            if (lastItem.equals(loopedItems.get(lastLoopedItemIndex))) {
                loopedItems.remove(lastLoopedItemIndex);
                itemsToBreakOn.remove(lastLoopedItemIndex);
                isLoopDetected = loopedItems.isEmpty();
            }
        }
    }

    /**
     * Checks if received type meta is the item on which looping should be broken.
     *
     * @param typeMeta <code>TypeMeta</code> to check.
     * @return <code>true</code> if received <code>TypeMeta</code> is the item on which looping should be broken,
     * <code>false</code> otherwise.
     */
    public boolean shouldBreakOnItem(TypeMeta<?> typeMeta) {
        if (!isLoopDetected) {
            return false;
        }
        return itemsToBreakOn.contains(typeMeta) || equalToAnyButNotTheSame(loopedItems, typeMeta);
    }

    private void checkForLooping(TypeMeta<?> typeMeta) {
        if (trace.indexOf(typeMeta) != trace.size() - 1) {
            isLoopDetected = true;
            loopedItems.add(typeMeta);
            itemsToBreakOn.add(loopBreakingStrategy.pickLoopBreakingItem(trace));
        }
    }

    private boolean equalToAnyButNotTheSame(List<TypeMeta<?>> typeMetaList, TypeMeta<?> typeMeta) {
        int indexOfItem = typeMetaList.indexOf(typeMeta);
        if (indexOfItem == -1) {
            return false;
        }
        return typeMetaList.get(indexOfItem) != typeMeta;
    }
}
