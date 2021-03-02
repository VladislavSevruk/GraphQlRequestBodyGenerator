package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlVariableType;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@GqlVariableType(variableType = "InputData", variableName = "search",
        defaultValue = "{\"name\":\"Test Name\",\"address\":\"Test Address\"}")
public class TestDataForInputTypeStrategy {

    @GqlField
    private String address;
    @GqlField
    private String name;
}
