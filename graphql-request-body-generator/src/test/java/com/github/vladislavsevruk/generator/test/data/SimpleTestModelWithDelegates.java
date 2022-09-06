package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SimpleTestModelWithDelegates {

    @GqlDelegate
    private NestedTestInputModel nestedTestModel;

    @GqlDelegate
    public NestedTestInputModel methodDelegate() {
        return new NestedTestInputModel().setNestedValue("nested value from method");
    }
}
