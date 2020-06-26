package com.github.vladislavsevruk.generator.test.data.loop;

import com.github.vladislavsevruk.generator.annotation.GqlField;

public class LongLoopedItem3 {

    @GqlField
    private long field3;
    @GqlField(withSelectionSet = true)
    private LongLoopedItem1 longLoopedItem1;
}
