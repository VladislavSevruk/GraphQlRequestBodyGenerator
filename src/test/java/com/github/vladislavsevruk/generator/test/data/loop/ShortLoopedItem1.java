package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class ShortLoopedItem1 {

    @GqlField
    private long field1;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem2[] shortLoopedItem2;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem3[] shortLoopedItem3;
}
