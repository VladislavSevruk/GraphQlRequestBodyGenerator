package com.github.vladislavsevruk.generator.generator.query;

import com.github.vladislavsevruk.generator.param.GqlArgument;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class GqlQueryArgumentsGeneratorTest {

    @Test
    void generateArgumentWithNewLinesTest() {
        GqlQueryArgumentsGenerator generator = new GqlQueryArgumentsGenerator();
        GqlArgument<?> argument = GqlArgument.of("argument", "value\nwith\nnew\nlines");
        String result = generator.generate(VariableGenerationStrategy.BY_ARGUMENT_TYPE.getVariablePickingStrategy(),
                Collections.singleton(argument));
        String expectedResult = "(argument:\"value\\nwith\\nnew\\nlines\")";
        Assertions.assertEquals(expectedResult, result);
    }
}
