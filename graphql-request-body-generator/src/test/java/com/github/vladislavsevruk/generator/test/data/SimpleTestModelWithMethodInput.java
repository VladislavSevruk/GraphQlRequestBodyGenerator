package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlInput;

public class SimpleTestModelWithMethodInput {

    @GqlInput(name = "")
    public String testValue() {
        return "testValue";
    }
}
