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
package com.github.vladislavsevruk.generator.strategy.marker;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages model fields picking strategies.<br> There are two field marking strategies for query body generation:<ul>
 * <li> pick all fields except ones that have {@link GqlIgnore} annotation
 * <li> pick only fields that have {@link GqlField} or {@link GqlDelegate} annotations
 * </ul>
 *
 * @see GqlField
 * @see GqlDelegate
 * @see GqlIgnore
 */
public final class FieldMarkingStrategyManager {

    private static final ReadWriteLock STRATEGY_LOCK = new ReentrantReadWriteLock();
    private static final Logger logger = LogManager.getLogger(FieldMarkingStrategyManager.class);
    private static FieldMarkingStrategy strategy = new AllExceptIgnoredFieldMarkingStrategy();

    private FieldMarkingStrategyManager() {
    }

    /**
     * Returns current picking strategy.
     */
    public static FieldMarkingStrategy getStrategy() {
        STRATEGY_LOCK.readLock().lock();
        FieldMarkingStrategy strategyToReturn = strategy;
        STRATEGY_LOCK.readLock().unlock();
        return strategyToReturn;
    }

    /**
     * Set picking all fields except ones that marked by {@link GqlIgnore} annotation as current picking strategy.
     */
    public static void useAllExceptIgnoredFieldsStrategy() {
        STRATEGY_LOCK.writeLock().lock();
        logger.info("Using all fields except ignored ones marking strategy.");
        strategy = new AllExceptIgnoredFieldMarkingStrategy();
        STRATEGY_LOCK.writeLock().unlock();
    }

    /**
     * Set picking only fields that marked by {@link GqlField} or {@link GqlDelegate} annotations as current picking
     * strategy.
     */
    public static void useOnlyMarkedFieldsStrategy() {
        STRATEGY_LOCK.writeLock().lock();
        logger.info("Using only marked fields marking strategy.");
        strategy = new OnlyMarkedFieldMarkingStrategy();
        STRATEGY_LOCK.writeLock().unlock();
    }
}
