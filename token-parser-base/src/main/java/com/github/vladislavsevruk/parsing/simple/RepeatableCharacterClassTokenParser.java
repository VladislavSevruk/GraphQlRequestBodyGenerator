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
package com.github.vladislavsevruk.parsing.simple;

import com.github.vladislavsevruk.parsing.LazyTokenNodeInitializer;
import com.github.vladislavsevruk.parsing.TokenNode;
import com.github.vladislavsevruk.parsing.TokenParser;
import com.github.vladislavsevruk.parsing.TokenParsingResult;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

import static com.github.vladislavsevruk.parsing.LazyTokenNodeInitializer.empty;

/**
 * Detects whether provided input contains one or more characters in a row at received position that match some
 * predefined condition. Parsing result will never have intermediate state, meaning that associated {@link TokenNode}
 * will only be created if specified condition was matched at least once. If provided input is not sufficient to
 * determine whether matched token end is reached, either additional input will be requested using received parent node
 * and start index or, if no such input expected, `match` result covering matched area will be returned straight away.
 * Implementation is also using optional lookahead rule that can be used to determine that specific lexical token
 * finished, e.g. to differentiate desired integer token from floating point number token.
 *
 * @param <T> type of {@link TokenNode} that will be initialized on match
 */
public class RepeatableCharacterClassTokenParser<T extends TokenNode> implements TokenParser {
    private final Predicate<Character> predicate;
    private final LazyTokenNodeInitializer<T> tokenNodeInitializer;
    @Nullable
    private final Predicate<Character> lookaheadPredicate;
    private final boolean isSignificant;

    private RepeatableCharacterClassTokenParser(Predicate<Character> predicate,
                                                LazyTokenNodeInitializer<T> tokenNodeInitializer,
                                                @Nullable Predicate<Character> lookaheadPredicate,
                                                boolean isSignificant)
    {
        this.predicate = predicate;
        this.tokenNodeInitializer = tokenNodeInitializer;
        this.lookaheadPredicate = lookaheadPredicate;
        this.isSignificant = isSignificant;
    }

    /**
     * Creates token parser for specific character condition detection without using lookahead symbol check and
     * returning {@link SimpleTokenNode#empty()} on match.
     *
     * @param predicate expected character condition to match
     * @return new instance set for specific character condition detection
     */
    public static RepeatableCharacterClassTokenParser<TokenNode> repeatableCharacterClass(Predicate<Character> predicate) {
        return repeatableCharacterClass(predicate, empty());
    }

    /**
     * Creates token parser for specific character condition detection returning {@link SimpleTokenNode#empty()}  on
     * match. Received {@code lookaheadPredicate} is used to check symbol following detected area to avoid
     * false-positive matches. E.g. to differentiate desired integer token from floating point number token using dot or
     * exponent symbol lookahead.
     *
     * @param predicate          expected character condition to match
     * @param lookaheadPredicate special rule for verifying symbol following detected match area
     * @return new instance set for specific character condition detection
     */
    public static RepeatableCharacterClassTokenParser<TokenNode> repeatableCharacterClass(Predicate<Character> predicate,
                                                                                          @Nullable Predicate<Character> lookaheadPredicate)
    {
        return repeatableCharacterClass(predicate, empty(), lookaheadPredicate);
    }

    /**
     * Creates token parser for specific character condition detection without using lookahead symbol check.
     *
     * @param predicate            expected character condition to match
     * @param tokenNodeInitializer initialization function used for specific {@link TokenNode} creation for matched
     *                             value
     * @param <T>                  type of {@link TokenNode} to initialize on match
     * @return new instance set for specific character condition detection
     */
    public static <T extends TokenNode> RepeatableCharacterClassTokenParser<T> repeatableCharacterClass(Predicate<Character> predicate,
                                                                                                        LazyTokenNodeInitializer<T> tokenNodeInitializer)
    {
        return repeatableCharacterClass(predicate, tokenNodeInitializer, null);
    }

    /**
     * Creates token parser for specific character condition detection. Received {@code lookaheadPredicate} is used to
     * check symbol following detected area to avoid false-positive matches. E.g. to differentiate desired integer token
     * from floating point number token using dot or exponent symbol lookahead.
     *
     * @param predicate            expected character condition to match
     * @param tokenNodeInitializer initialization function used for specific {@link TokenNode} creation for matched
     *                             value
     * @param lookaheadPredicate   special rule for verifying symbol following detected match area
     * @param <T>                  type of {@link TokenNode} to initialize on match
     * @return new instance set for specific character condition detection
     */
    public static <T extends TokenNode> RepeatableCharacterClassTokenParser<T> repeatableCharacterClass(Predicate<Character> predicate,
                                                                                                        LazyTokenNodeInitializer<T> tokenNodeInitializer,
                                                                                                        @Nullable Predicate<Character> lookaheadPredicate)
    {
        return repeatableCharacterClass(predicate, tokenNodeInitializer, lookaheadPredicate, true);
    }

    /**
     * Creates token parser for specific character condition detection. Received {@code lookaheadPredicate} is used to
     * check symbol following detected area to avoid false-positive matches. E.g. to differentiate desired integer token
     * from floating point number token using dot or exponent symbol lookahead. Received {@code isSignificant} flag
     * indicates whether matched area is having significant value that or can be omitted to optimize parsing of
     * following additional input.
     *
     * @param predicate            expected character condition to match
     * @param tokenNodeInitializer initialization function used for specific {@link TokenNode} creation for matched
     *                             value
     * @param lookaheadPredicate   special rule for verifying symbol following detected match area
     * @param isSignificant        flag indicating whether index for `needMoreTokens` result should point to the region
     *                             start or end
     * @param <T>                  type of {@link TokenNode} to initialize on match
     * @return new instance set for specific character condition detection
     */
    public static <T extends TokenNode> RepeatableCharacterClassTokenParser<T> repeatableCharacterClass(Predicate<Character> predicate,
                                                                                                        LazyTokenNodeInitializer<T> tokenNodeInitializer,
                                                                                                        @Nullable Predicate<Character> lookaheadPredicate,
                                                                                                        boolean isSignificant)
    {
        return new RepeatableCharacterClassTokenParser<>(predicate, tokenNodeInitializer, lookaheadPredicate,
                isSignificant);
    }

    /**
     * Parses received {@link CharSequence} input starting from the received index.
     *
     * @param content       input to process
     * @param startIndex    position (inclusive) of input to start parsing from
     * @param hasMoreTokens identifies if additional input can be provided by request
     * @return parsing result of provided input from received start position
     * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than {@code length}
     */
    @Override
    public TokenParsingResult parse(CharSequence content, final int startIndex, final boolean hasMoreTokens) {
        int indexAfterLastParsed = -1;
        for (int index = startIndex; index < content.length(); ++index) {
            if (!predicate.test(content.charAt(index))) {
                break;
            }
            indexAfterLastParsed = index + 1;
        }
        if (indexAfterLastParsed == -1) {
            return TokenParsingResult.mismatch();
        }
        if (indexAfterLastParsed == content.length()) {
            if (hasMoreTokens) {
                int lastIndex = isSignificant ? startIndex : content.length();
                return TokenParsingResult.needMoreTokens(SimpleTokenNode.empty(), lastIndex);
            }
            return match(content, startIndex, content.length());
        }
        if (lookaheadPredicate == null) {
            return match(content, startIndex, indexAfterLastParsed);
        }
        return lookaheadPredicate.test(content.charAt(indexAfterLastParsed)) ? match(content, startIndex,
                indexAfterLastParsed) : TokenParsingResult.mismatch();
    }

    private TokenParsingResult match(CharSequence content, int startIndex, int endIndex) {
        return TokenParsingResult.match(tokenNodeInitializer.initialize(content, startIndex, endIndex), endIndex);
    }
}
