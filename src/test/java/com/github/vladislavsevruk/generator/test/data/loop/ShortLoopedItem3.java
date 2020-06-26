package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class ShortLoopedItem3 {

    @GqlField
    private long field3;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem1 shortLoopedItem1;
    @GqlField(withSelectionSet = true)
    private ShortLoopedItem2 shortLoopedItem2;
}
