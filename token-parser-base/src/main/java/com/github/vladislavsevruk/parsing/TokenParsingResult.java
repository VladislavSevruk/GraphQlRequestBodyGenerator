/*
 * MIT License
 *
 * Copyright (c) 2026 Uladzislau Seuruk
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
package com.github.vladislavsevruk.parsing;

import com.github.vladislavsevruk.parsing.simple.SimpleTokenNode;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents outcome of token parsing attempt: whether it was successful or not or if it's impossible to make a
 * decision without additional input. Unsuccessful parsing will have no {@link TokenNode} and will point to -1 index,
 * while successful one with have parsed {@link TokenNode} and will point to its end index at input (exclusive).
 * For some special cases like parsing of optional token or single symbol identification {@link SimpleTokenNode#empty()}
 * may be returned. For case when {@link TokenParser} cannot make a certain decision, result will point to input index
 * where area of uncertainty starts will have {@link TokenNode} that is currently in progress.
 */
public final class TokenParsingResult {
    private static final TokenParsingResult MISMATCH = new TokenParsingResult(null, -1, false);
    private static final Pool POOL = new Pool();
    @Nullable
    private TokenNode token;
    /**
     * -- GETTER --
     * Returns last index of input that was successfully processed - can be interpreted as start index (inclusive) for
     * next parser or for current parsing operation resumption, or end index (exclusive) of last parsed
     * {@link TokenNode}.
     *
     * @return last index of input that was successfully processed
     */
    @Getter
    private int lastIndex;
    /**
     * -- GETTER --
     * Identifies if provided input was not sufficient to determine whether specific {@link TokenNode} was either
     * identified and/or fully parsed and if additional input should be provided to complete operation.
     *
     * @return {@code true} if additional input is required to complete the operation, {@code false} otherwise.
     */
    @Getter
    private boolean moreTokensNeeded;

    private TokenParsingResult(@Nullable TokenNode token, int lastIndex, boolean moreTokensNeeded) {
        this.token = token;
        this.lastIndex = lastIndex;
        this.moreTokensNeeded = moreTokensNeeded;
    }

    /**
     * Returns an instance that corresponds to failing attempt of some {@link TokenNode} parsing. Such instance will
     * have no {@link TokenNode} and will point to -1 index.
     *
     * @return instance that corresponds to `mismatch` result
     */
    public static TokenParsingResult mismatch() {
        return MISMATCH;
    }

    /**
     * Returns an instance that corresponds to successful attempt of some {@link TokenNode} parsing. Such instance will
     * have completed {@link TokenNode} and will point to index where that token ended (exclusive).
     *
     * @param token     successfully parsed completed token
     * @param lastIndex end index (exclusive) of parsed token
     * @return instance that corresponds to `match` result for specific token at specific position
     * @throws IllegalArgumentException if provided token node is not completed or if provided index is negative
     */
    public static TokenParsingResult match(TokenNode token, int lastIndex) {
        if (lastIndex < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        if (!token.isCompleted()) {
            throw new IllegalArgumentException("Token node is not completed yet");
        }
        return POOL.get(token, lastIndex, false);
    }

    /**
     * Returns an instance that corresponds to parsing attempt of some {@link TokenNode} that requires provision of
     * additional input. This can happen if parser either wasn't able to make a decision whether this token can be
     * parsed at all or was interrupted due to reaching the end of previous input and wants to continue parsing from
     * that point. Such instance will have incompleted {@link TokenNode} that is currently in progress and will point
     * to index where area of uncertainty or interruption starts (inclusive).
     *
     * @param token     incompleted token that is currently in progress
     * @param lastIndex start index (inclusive) of area of uncertainty or interruption
     * @return instance that corresponds to `needMoreTokens` result for specific token at specific position
     * @throws IllegalArgumentException if provided token node is completed or if provided index is negative
     */
    public static TokenParsingResult needMoreTokens(TokenNode token, int lastIndex) {
        if (lastIndex < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        if (token.isCompleted() && !token.isEmpty()) {
            throw new IllegalArgumentException("Token node is already completed");
        }
        return POOL.get(token, lastIndex, true);
    }

    /**
     * This utility method clears from cache all links to instances that were stored for further re-usage to let GC
     * destroy them.
     */
    public static void clearCaches() {
        POOL.initializedTokens.clear();
    }

    /**
     * Returns {@link TokenNode} that was parsed either partially or completely. For some special parsing cases like
     * optional token parsing or single symbol identification {@link SimpleTokenNode#empty()} may be returned. This
     * method can return value only for `match` and `needMoreTokens` results.
     *
     * @param <T> type of token to return
     * @return either partially or completely parsed token value
     * @throws IllegalStateException if was called for `mismatch` result
     */
    public <T extends TokenNode> T getToken() {
        if (token == null) {
            throw new IllegalStateException("Cannot get token for mismatch");
        }
        //noinspection unchecked
        return (T) token;
    }

    /**
     * Identifies if provided input was sufficient and matched expected specification to identify and fully parse
     * specific {@link TokenNode}.
     *
     * @return {@code true} if input was successfully parsed, {@code false} otherwise.
     */
    public boolean isMatched() {
        return !isMismatched() && !isMoreTokensNeeded();
    }

    /**
     * Identifies if provided input didn't match {@link TokenNode} specifications and if it cannot be parsed from such
     * input by {@link TokenParser}.
     *
     * @return {@code true} if input doesn't match token specifications, {@code false} otherwise.
     */
    public boolean isMismatched() {
        return lastIndex == -1;
    }

    /**
     * Returns alternative `match` result with provided parameters if current result is `mismatch`. For other result
     * types current instance will be returned.
     *
     * @param token     alternative token or {@link SimpleTokenNode#empty} instance
     * @param lastIndex end index (exclusive) of previous or alternative token
     * @return {@code this} instance if it's not `mismatch`, `match` instance with received parameters otherwise.
     * @throws IllegalArgumentException if current instance is `mismatch` and provided token node is completed or if
     *                                  provided index is negative
     */
    public TokenParsingResult orElse(TokenNode token, int lastIndex) {
        return isMismatched() ? match(token, lastIndex) : this;
    }

    /**
     * This utility method can be used to highlight that processing of this instance was finished and that it can be
     * reused for preparing return value of {@link #match(TokenNode, int)} and {@link #needMoreTokens(TokenNode, int)}
     * methods to reduce memory consumption on new instances creation. Usage of this method is totally optional, and it
     * may be better to let GC destroy instance if no further parsing operations are expected. Calling on `mismatch`
     * result will make no effect.
     */
    public void utilize() {
        if (this != MISMATCH) {
            POOL.release(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (isMismatched()) {
            return "TokenParsingResult.mismatch";
        }
        return "TokenParsingResult." + (isMoreTokensNeeded()
                ? "needMoreTokens(" + lastIndex + ")"
                : "match(" + lastIndex + ")");
    }

    private static final class Pool {
        private final Queue<TokenParsingResult> initializedTokens = new LinkedList<>();

        private Pool() {
        }

        private TokenParsingResult get(TokenNode token, int lastIndex, boolean moreTokensNeeded) {
            TokenParsingResult tokenParsingResult = initializedTokens.poll();
            if (tokenParsingResult == null) {
                return new TokenParsingResult(token, lastIndex, moreTokensNeeded);
            }
            tokenParsingResult.token = token;
            tokenParsingResult.lastIndex = lastIndex;
            tokenParsingResult.moreTokensNeeded = moreTokensNeeded;
            return tokenParsingResult;
        }

        private void release(TokenParsingResult tokenParsingResult) {
            tokenParsingResult.token = null;
            initializedTokens.add(tokenParsingResult);
        }
    }
}
