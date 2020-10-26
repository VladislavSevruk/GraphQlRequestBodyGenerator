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

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of <code>FieldMarkingStrategyManager</code>.
 *
 * @see FieldMarkingStrategyManager
 */
@Log4j2
public class FieldMarkingStrategyManagerImpl implements FieldMarkingStrategyManager {

    private FieldMarkingStrategy strategy = new AllExceptIgnoredFieldMarkingStrategy();
    private final ReadWriteLock strategyLock = new ReentrantReadWriteLock();

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldMarkingStrategy getStrategy() {
        strategyLock.readLock().lock();
        FieldMarkingStrategy strategyToReturn = strategy;
        strategyLock.readLock().unlock();
        return strategyToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllExceptIgnoredFieldsStrategy() {
        strategyLock.writeLock().lock();
        log.info("Using all fields except ignored ones marking strategy.");
        strategy = new AllExceptIgnoredFieldMarkingStrategy();
        strategyLock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCustomStrategy(FieldMarkingStrategy customStrategy) {
        strategyLock.writeLock().lock();
        log.info("Using custom marking strategy.");
        strategy = customStrategy;
        strategyLock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useOnlyMarkedFieldsStrategy() {
        strategyLock.writeLock().lock();
        log.info("Using only marked fields marking strategy.");
        strategy = new OnlyMarkedFieldMarkingStrategy();
        strategyLock.writeLock().unlock();
    }
}
