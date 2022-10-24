package com.github.vladislavsevruk.generator.generator.mutation;

import com.github.vladislavsevruk.generator.param.GqlInputArgument;
import com.github.vladislavsevruk.generator.strategy.argument.ModelArgumentGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.marker.AllExceptIgnoredFieldMarkingStrategy;
import com.github.vladislavsevruk.generator.strategy.picker.mutation.InputGenerationStrategy;
import com.github.vladislavsevruk.generator.strategy.variable.VariableGenerationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class GqlMutationArgumentsGeneratorTest {

    @Test
    void generateArgumentWithNewLinesTest() {
        GqlMutationArgumentsGenerator generator
                = new GqlMutationArgumentsGenerator(new AllExceptIgnoredFieldMarkingStrategy());
        GqlInputArgument<?> argument = GqlInputArgument.of("value\nwith\nnew\nlines");
        String result = generator.generate(InputGenerationStrategy.WITHOUT_NULLS.getInputFieldsPickingStrategy(),
                ModelArgumentGenerationStrategy.ANY_ARGUMENT.getModelArgumentStrategy(),
                VariableGenerationStrategy.BY_ARGUMENT_TYPE.getVariablePickingStrategy(),
                Collections.singleton(argument));
        String expectedResult = "(input:\"value\\nwith\\nnew\\nlines\")";
        Assertions.assertEquals(expectedResult, result);
    }
}
