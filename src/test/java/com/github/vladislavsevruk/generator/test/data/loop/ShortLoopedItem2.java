package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class ShortLoopedItem2 {

    @GqlField
    private long field2;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem1 shortLoopedItem1;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem3[] shortLoopedItem3;
}
