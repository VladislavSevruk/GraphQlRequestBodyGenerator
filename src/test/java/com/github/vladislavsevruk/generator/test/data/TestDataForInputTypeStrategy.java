package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlInputType;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@GqlInputType(inputType = "InputData")
public class TestDataForInputTypeStrategy {

    @GqlField
    private String name;

    @GqlField
    private String address;
}
