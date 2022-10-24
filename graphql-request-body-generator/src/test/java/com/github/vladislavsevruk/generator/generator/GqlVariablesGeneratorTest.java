package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.param.GqlVariableArgument;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class GqlVariablesGeneratorTest {

    @Test
    void generateVariableWithNewLinesTest() {
        GqlVariablesGenerator generator = new GqlVariablesGenerator(new AllExceptIgnoredFieldMarkingStrategy());
        GqlVariableArgument<?> argument = GqlVariableArgument.of("variable", "value\nwith\nnew\nlines");
        String result = generator.generate(InputGenerationStrategy.ALL_FIELDS.getInputFieldsPickingStrategy(),
                VariableGenerationStrategy.BY_ARGUMENT_TYPE.getVariablePickingStrategy(),
                Collections.singleton(argument));
        String expectedResult = "{\"variable\":\"value\\nwith\\nnew\\nlines\"}";
        Assertions.assertEquals(expectedResult, result);
    }
}
