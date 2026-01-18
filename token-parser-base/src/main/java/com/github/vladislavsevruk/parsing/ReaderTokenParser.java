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

import com.github.vladislavsevruk.parsing.exception.ParsingInputException;
import com.github.vladislavsevruk.parsing.util.CompositeCharSequence;

import java.io.IOException;
import java.io.Reader;

import static com.github.vladislavsevruk.parsing.simple.SimpleTokenNode.empty;

/**
 * Parses input characters read from provided {@link Reader} according to rules specified by some {@link TokenParser}.
 * This class doesn't open any input streams or readers so doesn't bother to close any as well, please be aware.
 */
public class ReaderTokenParser {
    private final TokenParser tokenParser;
    private final int bufferSize;

    /**
     * Creates new instance with default input buffer size and specific token parser that specifies parsing rules of any
     * received input.
     *
     * @param tokenParser token parser that specifies parsing rules
     */
    public ReaderTokenParser(TokenParser tokenParser) {
        this(tokenParser, 8_192);
    }

    /**
     * Creates new instance with specified input buffer size and specific token parser that specifies parsing rules of
     * any received input.
     *
     * @param tokenParser token parser that specifies parsing rules
     * @param bufferSize  input buffer size
     */
    public ReaderTokenParser(TokenParser tokenParser, int bufferSize) {
        this.tokenParser = tokenParser;
        this.bufferSize = bufferSize;
    }

    /**
     * Parses input characters read from provided input source according to rules specified by token parser.
     *
     * @param reader source of character input to read from
     * @return parsed token node according to rules specified by token parser
     * @throws IOException if an I/O error occurs
     */
    public TokenNode parse(Reader reader) throws IOException {
        char[] buffer = new char[bufferSize];
        int read = reader.read(buffer);
        if (read == -1) {
            return empty();
        }
        CompositeCharSequence compositeCharSequence = new CompositeCharSequence(buffer, read);
        boolean lastHasMoreInputSignal = read == buffer.length;
        TokenParsingResult result = tokenParser.parse(compositeCharSequence, lastHasMoreInputSignal);
        if (result.isMoreTokensNeeded()) {
            compositeCharSequence.preSaveLeftover(result.getLastIndex());
            while ((read = reader.read(buffer)) != -1) {
                compositeCharSequence.setCurrent(buffer, read);
                lastHasMoreInputSignal = read == buffer.length;
                result = resumeParsing(compositeCharSequence, result, lastHasMoreInputSignal);
                if (!result.isMoreTokensNeeded()) {
                    break;
                }
                compositeCharSequence.preSaveLeftover(result.getLastIndex());
            }
            if (lastHasMoreInputSignal && result.isMoreTokensNeeded()) {
                result = resumeParsing(compositeCharSequence, result, false);
            }
        }
        if (!result.isMatched()) {
            throw new ParsingInputException("Failed to parse received input");
        }
        TokenNode tokenNode = result.getToken();
        TokenParsingResult.clearCaches();
        return tokenNode;
    }

    private TokenParsingResult resumeParsing(CompositeCharSequence compositeCharSequence,
                                             TokenParsingResult lastResult,
                                             boolean hasMoreInput)
    {
        TokenNode tokenNode = lastResult.getToken();
        lastResult.utilize();
        return tokenNode.resumeParsing(compositeCharSequence, hasMoreInput);
    }
}
