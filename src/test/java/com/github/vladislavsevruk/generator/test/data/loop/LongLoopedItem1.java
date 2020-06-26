package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class LongLoopedItem1 {

    @GqlField
    private long field1;
    @GqlField(withSelectionSet = true)
    private LongLoopedItem2 longLoopedItem2;
}
